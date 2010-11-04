/*!
 * dsHistory, v1-beta5 $Rev: 78 $
 * Revision date: $Date: 2008-12-08 14:46:08 -0800 (Mon, 08 Dec 2008) $
 * Project URL: http://code.google.com/p/dshistory/
 * 
 * Copyright (c) Andrew Mattie (http://www.akmattie.net)
 * Licensed under the MIT License (http://www.opensource.org/licenses/mit-license.php)
 * THIS IS FREE SOFTWARE, BUT DO NOT REMOVE THIS COMMENT BLOCK
 */

var dsHistory = function() {
	// we need a good browser detection library. these detections were kindly borrowed from the Prototype library
	var browser = (function() {
		var userAgent = window.navigator.userAgent;
		var isIE = !!(window.attachEvent && !window.opera && userAgent.indexOf('Opera') == -1);
		
		return {
			IE: isIE,
			IE6: isIE && userAgent.indexOf('MSIE 6') != -1,
			IE7: isIE && userAgent.indexOf('MSIE 7') != -1,
			Opera: !!window.opera && userAgent.indexOf('Opera') != -1,
			WebKit: userAgent.indexOf('AppleWebKit/') > -1,
			Gecko: userAgent.indexOf('Gecko') > -1 && userAgent.indexOf('KHTML') == -1
		};
	})();
	var supportsChangingHistoryViaFrame = browser.IE || browser.Gecko;
	var supportsDataProtocol = browser.Gecko; // other browsers may support the data protocol, but if they don't support changing the history via a frame, they aren't in here
	var returnsEncodedWindowHash = browser.IE || browser.WebKit; // some browsers return the encoded value of the window hash vs the decoded value
	var fluxCapacitorInterval = 15;
	var lastFrameIteration = 0;
	var lastHash = lastRawHash = '';
	var encodeURIComponent = window.encodeURIComponent; // close a reference to this function since we'll be calling it so often and since it will be faster than going up the scope each time
	var dirtyHash = initialHash = getEncodedWindowHash(true);
	var hashCache = []; // holds all previous hashes
	var forwardHashCache = []; // hashes that are removed from hashCache as the user goes back are concat'd here
	var eventCache = []; // holds all events
	var forwardEventCache = []; // events that are removed from eventCache as the user goes back are concat'd here
	var isInHistory = false; // if we're somewhere in the middle of the history stack, this will be set to true
	var frameWindow; // since we're going to be looking at the internals of the frame so often, we will cache a reference to it and just unload it when the page unloads
	// if the reference to this script file is included in the head tag, frameWindow won't be set up properly. this will be a reference to the setInterval that will ultimately set up frameWindow in that case
	var frameWindowWatcher;
	var executionQueue = []; // if the frameWindow wasn't set up on load, we need a place to queue up actions until it's available
	var watcherInterval; // save the handle returned by setInterval so we can unregister it on unload
	var isGoingBackward, isGoingForward; // assists us in knowing whether we're going back through the history or forward
	var usingStringIndicators = false; // if the developer called dsHistory.setUsingStringIndicators(), this will be set to true
	var returnObject;
	
	// internal function to make sure we don't leave any memory leaks when the visitor leaves
	function unload() {
		window.clearInterval(watcherInterval);
		frameWindow = null;
		eventCache = null;
	};
	// internal function to curry the scope argument and object argument (if either) so that the subscriber can be called once it's appropriately hit in the
	// history
	function internalCurry(fnc, scope, objectArg) {
		if (typeof objectArg != 'undefined') {
			return function(historyObj) {
				fnc.call(scope || window, objectArg, historyObj);
			};
		} else {
			return function(historyObj) {
				fnc.call(scope || window, historyObj);
			};
		}
	};
	// internal function to return the iteration we are on
	function readIteration() {
		// lazy function definition pattern used for performance
		if (!supportsChangingHistoryViaFrame) {
			readIteration = function() {
				return 0;
			};
		} else if (supportsDataProtocol) {
			readIteration = function() {
				return frameWindow.document.body ? parseInt(frameWindow.document.body.textContent) : 0;
			};
		} else {
			readIteration = function() {
				return parseInt(frameWindow.document.body.innerText);
			};
		}
		
		return readIteration();
	};
	// internal function to save the iteration we are on
	function writeIteration(iteration) {
		// lazy function definition pattern used for performance
		if (supportsDataProtocol) {
			writeIteration = function(iteration) {
				frameWindow.document.body.textContent = String(iteration);
			};
		} else {
			writeIteration = function(iteration) {
				frameWindow.document.body.innerText = String(iteration);
			};
		}
		
		writeIteration(iteration);
	};
	// internal function to get the decoded value of a (sub)string from the hash
	function getDecodedHashValue(value) {
		if (returnsEncodedWindowHash) {
			var decodeURIComponent = window.decodeURIComponent;
			
			getDecodedHashValue = function(value) {
				return decodeURIComponent(value);
			};
		} else {
			getDecodedHashValue = function(value) {
				return value;
			};
		}
		
		return getDecodedHashValue(value);
	};
	// internal function to return the window hash after the keys and values have been run through encodeURIComponent
	function getEncodedWindowHash(forceRecompute) {
		var hash = window.location.hash;
		
		// there's no need to go through this function again if the hash that was read out (encoded or decoded, doesn't matter) is the same hash that was read out last time.
		// whenever lastHash is set, it's set to the return value of the function
		if (!forceRecompute && hash == lastRawHash) return lastHash;
		lastRawHash = hash;
		
		var hashItems = hash.substring(1).split('&');
		var encodedHash;
		
		// for performance, we'll assume that if we're doing more than 9 concats that it will be quicker to use and array and then use the .join('&') trick
		if (hashItems.length > 9) {
			var encodedHashItems = [];
			
			for (var i = 0, len = hashItems.length; i < len; ++i) {
				hashSplit = hashItems[i].split('=');
				encodedHashItems.push(encodeURIComponent(getDecodedHashValue(hashSplit[0])) + (hashSplit.length == 2 ? '=' + encodeURIComponent(getDecodedHashValue(hashSplit[1])) : ''));
			}
			encodedHash = encodedHashItems.join('&');
		} else {
			encodedHash = ''
			for (var i = 0, len = hashItems.length; i < len; ++i) {
				hashSplit = hashItems[i].split('=');
				encodedHash += (i == 0 ? '' : '&') + encodeURIComponent(getDecodedHashValue(hashSplit[0])) + (hashSplit.length == 2 ? '=' + encodeURIComponent(getDecodedHashValue(hashSplit[1])) : '');
			}
		}
		
		return encodedHash;
	};
	// internal function to load and split our query vars into our QueryElements object
	function loadQueryVars() {
		// flush out the object each time this is called
		returnObject.QueryElements = {};
		
		if (window.location.hash == '' || window.location.hash == '#') return;
		
		var hashItems = window.location.hash.substring(1).split('&');
		var hashSplit;
		
		for (i = 0, len = hashItems.length; i < len; ++i) {
			hashSplit = hashItems[i].split('=');
			returnObject.QueryElements[getDecodedHashValue(hashSplit[0])] = hashSplit.length == 2 ? getDecodedHashValue(hashSplit[1]) : '';
		}
		
		lastHash = getEncodedWindowHash(true);
	};
	// internal function to be called when we want to actually add something to the browser's history
	function updateFrameIteration(comingFromQueryBind) {
		var currentIteration = supportsChangingHistoryViaFrame ? readIteration() : 0;
		var lastEvent, newEvent;
		
		// it seems that gecko has a sweet bug / feature / something that prevents the history from changing with a frame iteration after a hash has changed the history
		// therefore, we have to mess with the hash enough to get it to add to the browser's history and then change it back so we don't screw up any values in the hash
		if ( (hashCache.length > 0 && browser.Gecko) || browser.WebKit || (!supportsChangingHistoryViaFrame && readIteration() > 0) ) {
			
			// since it's not IE, and since other browsers don't seem to have a performance problem with setting the window hash when there are lots of
			// elements on a page, we're not going to worry about handling the defer processing attribute here
			
			if (lastHash == '' && hashCache.length > 1) {
				window.location.hash = '_'; // this can be anything, as long as the hash changes
				lastHash = getEncodedWindowHash(true);
				hashCache.push(lastHash);
			} else if (lastHash != '' || browser.WebKit) {
				// splice the event off the stack so we can add it on later
				lastEvent = eventCache.splice(eventCache.length - 1, 1)[0];
				
				window.location.hash = lastHash + String(hashCache.length); // this can be anything, as long as the hash changes
				hashCache.push(lastHash + String(hashCache.length));
				
				// lastHash should only be empty if the browser is WebKit.
				window.location.hash = lastHash == '' ? '-' : lastHash; // the value if lastHash is empty can't be the same as the value in the if-case above
				hashCache.push(lastHash == '' ? '-' : lastHash);
				
				// since we popped off the last event on the history stack, we're going to add it back on _after_ we add on a function to get back to our unadultered hash
				eventCache.push(function(indicator) {
					if (usingStringIndicators ? indicator : indicator.direction == 'back') {
						isGoingBackward = true;
						window.history.back();
					} else {
						isGoingForward = true;
						window.history.forward();
					}
				});
				eventCache.push(lastEvent);
			}
			
			return;
		}
		
		// there's no reason to change the frame source if we're only adding the first event to the history
		if (
			currentIteration == 0
			&& ( (hashCache.length == (comingFromQueryBind ? 1 : 0) && !browser.IE) || (hashCache.length == 2 && browser.IE) ) // extra hash for ie
			&& eventCache.length <= 1
			) {
			writeIteration(1);
		} else {
			if (supportsDataProtocol)
				document.getElementById('dsHistoryFrame').src = 'data:,' + String(currentIteration + 1);
			else {
				frameWindow.document.open();
				frameWindow.document.write(String(currentIteration + 1));
				frameWindow.document.close();
			}
		}
	};
	// internal function that is called every few ms to check to see if we've gone back or forward in time
	function fluxCapacitor() {
		var frameIteration = supportsChangingHistoryViaFrame ? readIteration() : 0;
		var windowHash = getEncodedWindowHash();
		
		// if the frame iteration is different or the window hash is different, we'll start a sequence of events to go back in time
		if (
			!isGoingForward
			&& (
				frameIteration < lastFrameIteration
				|| (lastHash != windowHash && hashCache[hashCache.length - 2] == windowHash && !browser.IE)
				)
			) {
			
			// this will be the pre-qual for our people hitting the forward button
			isInHistory = true;
			isGoingBackward = false;
			
			// if the hash has changed, or if we're using IE (in which case we change the hash with every event), make sure we
			// keep the hashCache and related items up-to-date
			if ((lastHash != windowHash && hashCache[hashCache.length - 2] == windowHash) || browser.IE) {
				forwardHashCache = forwardHashCache.concat(hashCache.splice(hashCache.length - 1, 1));
				
				// IE doesn't change the window hash when the user goes back, so we have to do it manually from our hashCache
				if (browser.IE) {
					if (returnObject.deferProcessing) {
						window.setTimeout(function() { window.location.hash = hashCache[hashCache.length - 1]; }, 10);
					} else {
						window.location.hash = hashCache[hashCache.length - 1];
					}
				}
				
				// lastHash gets updated here so that if history.back() is one of the functions on the eventCache,
				// it will know we're on a different hash. additionally, QueryVars is reloaded to match the new hash
				loadQueryVars();
				dirtyHash = lastHash;
			}
			
			// subtract 2 from eventCache.length since we're gonna end up calling the second function from the end when someone clicks the
			// back button. we can assume that another function was pushed onto the stack since the time the function we are going to call was
			// added. essentially, the last function in the array is the function we are on now, so we need to ignore it in here.
			
			// all functions that are pushed onto the history stack must consume either the 'back' string or, preferrably, the object literal
			// containing the history event information. this must be done to prevent having the called function from pushing itself back onto
			// the history stack as soon as it is called.
			
			if (eventCache.length > 1) {
				eventCache[eventCache.length - 2](usingStringIndicators ? 'back' : { calledFromHistory: true, direction: 'back' });
				forwardEventCache = forwardEventCache.concat(eventCache.splice(eventCache.length - 1, 1));
			}
		}
		
		// handle the forward button. we determine whether we're moving forward if 1) the user hit the back button and we haven't added a
		// function or bound the query vars since, 2) we haven't hit our built-in history.back function to work around gecko's
		// updating-frame-doesnt-update-history-after-hash-has-been-added bug, and 3) the secondary conditions that allow us to know
		// whether we're going back in our history are inversed
		
		else if (
			isInHistory
			&& !isGoingBackward
			&& (
				frameIteration > lastFrameIteration
				|| (lastHash != windowHash && forwardHashCache[forwardHashCache.length - 1] == windowHash && !browser.IE)
				)
			) {
			isGoingForward = false;
			
			// the internals of this are nearly the same as the way we handle the visitor going back, except we use different caches
			// for reading the events and hashes we spliced off as we went back
			if ((lastHash != windowHash && forwardHashCache[forwardHashCache.length - 1] == windowHash) || browser.IE) {
				if (browser.IE)
					window.location.hash = forwardHashCache[forwardHashCache.length - 1];
				loadQueryVars();
				dirtyHash = lastHash;
				hashCache = hashCache.concat(forwardHashCache.splice(forwardHashCache.length - 1, 1));
			}
			
			// see the notes above about the called function consuming the argument
			forwardEventCache[forwardEventCache.length - 1](usingStringIndicators ? 'forward' : { calledFromHistory: true, direction: 'forward' });
			eventCache = eventCache.concat(forwardEventCache.splice(forwardEventCache.length - 1, 1));
		}
		
		// so we always have something to compare to the next time this is called
		lastFrameIteration = frameIteration;
	};
	
	returnObject = {
		QueryElements: {}, // name/value collection to hold the values in the window hash
		// if there are a ton of elements on a page, IE can chunk when the window hash is set since, i assume, it's trying to find the element on that
		// page that has a matching name attribute. while we can't speed it up any, we can at least appear to make it go faster by deferring the hash
		// setter until the next cycle so that the UI can update
		deferProcessing: false,
		initialize: function(initFnc) {
			// the library itself is actually initialized before the anonymous function that is this library is returned, but we use
			// this function call for backwards compatibility
			
			if (typeof initFnc == 'function') initFnc();
		},
		addFunction: function(fnc, scope, objectArg) {
			if (supportsChangingHistoryViaFrame && (!frameWindow || !frameWindow.document || !frameWindow.document.body)) {
				executionQueue.push({type: arguments.callee, fnc: fnc, scope: scope, objectArg: objectArg});
				return;
			}
			// flush out anything that would have been used for the forward action if the user had used the back action
			isInHistory = false;
			forwardEventCache = [];
			forwardHashCache = [];
			
			// with IE, we want to make sure they're a hash entry put into the cache every time we change the frame
			// since moving back won't change the location hash. we'll use the hash cache to manually change the cache then.
			if (browser.IE)
				hashCache.push(getEncodedWindowHash());
			
			eventCache.push(internalCurry(fnc, scope, objectArg));
			updateFrameIteration();
		},
		// this will conditionally add or update the name / value that was passed in. it will also add / update the QueryElements object
		setQueryVar: function(key, value) {
			var encodedKey, encodedValue;
			var indexOfKey;
			
			key = String(key);
			value = String(typeof value == 'undefined' ? '' : value);
			
			encodedKey = encodeURIComponent(key);
			encodedValue = encodeURIComponent(value);
			
			if (dirtyHash == '#' || dirtyHash == '' || dirtyHash.indexOf('#_serial') == 0) {
				if (encodedValue != '')
					dirtyHash = '#' + encodedKey + '=' + encodedValue;
				else
					dirtyHash = '#' + encodedKey;
			} else {
				if (typeof this.QueryElements[key] != 'undefined' && value != '') {
					indexOfKey = dirtyHash.search(encodedKey + '\\b');
					dirtyHash = dirtyHash.substr(0, dirtyHash.indexOf(encodedKey) + encodedKey.length + 1) + encodedValue + dirtyHash.substr(dirtyHash.indexOf(encodedKey) + encodedKey.length + 1 + String(encodeURIComponent(this.QueryElements[key])).length);
				} else if (typeof this.QueryElements[key] == 'undefined') {
					if (value == '')
						dirtyHash += '&' + encodedKey;
					else
						dirtyHash += '&' + encodedKey + '=' + encodedValue;
				}
			}
			
			this.QueryElements[key] = value;
			
			if (hashCache > 1 && hashCache[hashCache.length - 2] == dirtyHash)
				dirtyHash += '&_serial=' + hashCache.length;
			else if (dirtyHash.indexOf('_serial') != -1)
				this.removeQueryVar('_serial');
		},
		// this will remove the property of the QueryElements object and remove the name and value of the object in the dirtyHash
		removeQueryVar: function(key) {
			if (!this.QueryElements[key] && key != '_serial') return;
			
			var dataToStrip, indexOfData, removeAmpersand;
			
			if (this.QueryElements[key] == '')
				dataToStrip = encodeURIComponent(key);
			else
				dataToStrip = encodeURIComponent(key) + '=' + encodeURIComponent(this.QueryElements[key]);
			
			indexOfData = dirtyHash.indexOf(dataToStrip);
			if (dirtyHash[indexOfData - 1] == '&') {
				dataToStrip = '&' + dataToStrip;
				indexOfData--;
			}
			dirtyHash = dirtyHash.substr(0, indexOfData) + dirtyHash.substr(indexOfData + dataToStrip.length);
			if (dirtyHash[0] == '&')
				dirtyHash = dirtyHash.substr(1, dirtyHash.length - 1);
			
			delete this.QueryElements[key];
			
			// if the hash is empty, a serial number is appended to it so we can keep track of whether we're going forward or backward in the
			// frame watcher. this is needed specifically when a value is added, removed, and added again.
			if (dirtyHash == '#' || dirtyHash == '') dirtyHash = '_serial=' + hashCache.length;
		},
		// the time in Gecko browsers.
		// we don't want to update the window has until this function is called since, otherwise, the history will change all
		bindQueryVars: function(fnc, scope, objectArg, continueProcessing) {
			if (supportsChangingHistoryViaFrame && (!frameWindow || !frameWindow.document || !frameWindow.document.body)) {
				executionQueue.push({type: arguments.callee, fnc: fnc, scope: scope, objectArg: objectArg});
				return;
			}
			// if desired, one could check if the result of this function is === false and operate accordingly. shouldn't really be necessary though
			// dirty hash will always be encoded, so replace('#', '') will only replace the inital # if it's there
			if (getEncodedWindowHash() == dirtyHash.replace('#', '') && eventCache.length > 0) return false;
			
			// if the option to defer processing has been set and our continueProcessing argument has been set, defer the function call to the
			// next available cycle so that the UI can update and other processing can continue. 
			if (this.deferProcessing && !continueProcessing) {
				var currentFnc = arguments.callee;
				window.setTimeout(function() { currentFnc(fnc, scope, objectArg, true) }, 10);
				return;
			}
			
			// flush out anything that would have been used for the forward action if the user had used the back action
			isInHistory = false;
			forwardEventCache = [];
			forwardHashCache = [];
			
			// so we have an empty hash to go back to on our first time around (but only if we're not using IE since otherwise we're adding
			// to the hashCache every single time anyway)
			if (hashCache.length == 0 && eventCache.length > 0 && !browser.IE)
				hashCache.push(getEncodedWindowHash());
			
			window.location.hash = dirtyHash;
			lastHash = getEncodedWindowHash(true);
			
			hashCache.push(lastHash);
			eventCache.push(internalCurry(fnc, scope, objectArg));
			
			if (browser.IE)
				updateFrameIteration(true);
			
			loadQueryVars();
		},
		setFirstEvent: function(fnc, scope, objectArg) {
			if (eventCache.length > 0)
				eventCache[0] = internalCurry(fnc, scope, objectArg);
		},
		setUsingStringIndicators: function() {
			// use a setter function for this deprecated functionality instead of a property so that calls to this function will error once it's removed
			usingStringIndicators = true;
		}
	};
	
	// initialize the library
	
	// we use a frame to track history all the time in IE since window.location.hash doesn't update the history.
	// in Gecko, we only use a frame to track history when we're not also trying to update the window hash.
	// in WebKit, we mess with the hash just a little bit to change the history if they aren't explicitly changing the window hash
	if (supportsChangingHistoryViaFrame) {
		if (supportsDataProtocol)
			document.write('<iframe id="dsHistoryFrame" name="dsHistoryFrame" style="display:none" src="data:,0"></iframe>');
		else
			document.write('<iframe id="dsHistoryFrame" name="dsHistoryFrame" style="display:none" src="javascript:document.open();document.write(\'0\');document.close();"></iframe>');
		
		frameWindow = window.frames['dsHistoryFrame'];
		if (!frameWindow || !frameWindow.document || !frameWindow.document.body) {
			frameWindowWatcher = window.setInterval(function() {
				frameWindow = window.frames['dsHistoryFrame'];
				if (frameWindow && frameWindow.document && frameWindow.document.body) {
					window.clearInterval(frameWindowWatcher);
					watcherInterval = window.setInterval(fluxCapacitor, fluxCapacitorInterval);
					
					for (i = 0, len = executionQueue.length; i < len; ++i) {
						var executionItem = executionQueue[i];
						executionItem.type(executionItem.fnc, executionItem.scope, executionItem.objectArg);
					}
					executionQueue = null;
				}
			}, 50);
		} else {
			watcherInterval = window.setInterval(fluxCapacitor, fluxCapacitorInterval);
		}
	} else {
		watcherInterval = window.setInterval(fluxCapacitor, fluxCapacitorInterval);
	}
	
	if (browser.IE || browser.WebKit)
		hashCache.push(initialHash);
	//if (browser.WebKit && window.location.hash == '')
		//window.location.hash = '_';
	
	// initialize the QueryElements object
	loadQueryVars();
	
	// make sure we don't leave any memory leaks when the visitor leaves
	if (window.addEventListener)
		window.addEventListener('unload', unload, false);
	else if (window.attachEvent)
		window.attachEvent('onunload', unload);
	
	// end initialization
	
	return returnObject;
}();
