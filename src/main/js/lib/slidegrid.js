/*
File:           slidegrid.js
Description:    for more information see http://www.zackgrossbart.com/hackito/slidegrid/

*******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

slidegrid = {
	hasDrawn: false,
	windowWidth: 0,
	cellWidth: 250,
	cellHeight: 150,
	padding: 30,
	gridOffset: 0,
	
	/**
	 * Adds a specific cell to the set of used cells.  Used cells are skipped 
	 * when laying cells out in the future.
	 * 
	 * @param usedCells the array of used cells
	 * @param col       the column of the used cell
	 * @param row       the row of the used cell
	 */
	setUsed: function(/*array*/ usedCells, /*int*/col, /*int*/row) {
	    var index = usedCells.length;
	    
	    usedCells[index] = { left: col, top: row};
	},

	/**
	 * Lets you know if a specific column and row index has already been used.
	 * 
	 * @param usedCells the array of used cells
	 * @param col       the column of the cell to check
	 * @param row       the row of the cell to check
	 */
	isUsed: function(/*array*/ usedCells, /*int*/ col, /*int*/ row) {
	    for (var i = 0; i < usedCells.length; i++) {
	        if (usedCells[i].left === col &&
	            usedCells[i].top === row) {
	            return true;
	        }
	    }
	    
	    return false;
	},
	
	/**
	 * Applies the styling to a specific cell.
	 * 
	 * @param cell       The JQuery object representing the cell to style
	 * @param x          the left position of the cell in EMs
	 * @param y          the top position of the cell in EMs
	 * @param cellWidth  the width of the cell in EMs
	 * @param cellHeight the height of the cell in EMs
	 * @param animate    true if this style should be animated and false otherwise
	 */
	styleCell: function(cell, x, y, cellWidth, cellHeight, /*boolean*/animate) {
	    if (animate) {
	        cell.animate({ 
	            left: x + "px",
	            top: y + "px"
	            }, 500, "swing" );
	    } else {
	        cell.css({
	            width: cellWidth + "px",
	            height: cellHeight + "px",
	            position: "absolute",
	            left: x + "px",
	            top: y + "px"
	        });
	    }
	},

	/**
	 * Aligns a set of elements in a resizable grid.
	 * 
	 * @param cellWidth  the width of each cell in px
	 * @param cellHeight the height of each cell in px
	 * @param padding    the padding between each cell in px
	 * @param gridOffset the offset width of the grid in px
	 */
	alignGrid: function(/*int*/ cellWidth, /*int*/ cellHeight, /*int*/ padding, /*int*/ gridOffset) {
		slidegrid.cellWidth = cellWidth;
		slidegrid.cellHeight = cellHeight;
		slidegrid.padding = padding;
		slidegrid.gridOffset = gridOffset;
		
	    var x = padding / 2;
	    var y = 0;
	    var count = 1;
	    
	    /* 
	     * When we add a "bigcell" to the grid it takes up four cells instead of one. 
	     * That makes we need to not add any other cells to those areas or the cells 
	     * will overlap.  These three variables are used to remember the cells used 
	     * by big cells. 
	     */
	    var curCol = 0;
	    var curRow = 0;
	    var usedCells = [];
	    
	    $(".slidegrid").each(function() {

	        var hasTallCell = false;
	        
	        var cols = Math.floor(($(window).width() - gridOffset) / ((cellWidth + padding)));
			
			$(this).css("position", "relative");
	        
	        var children = $(this).children("div");
			
			for (var i = 0; i < children.length; i++) {
				if (children.eq(i).hasClass("bigcell")) {
	                if (curCol === cols - 1) {
	                    /* 
	                     * This means it is time to bump down to the next row
	                     */
	                    curCol = 0;
	                    curRow++;
	                    x = padding / 2;
	                    y += cellHeight + padding;
	                    count++;
	                    
	                }

	                if (cols > 1 && (slidegrid.isUsed(usedCells, curCol, curRow) ||
	                    slidegrid.isUsed(usedCells, curCol + 1, curRow) ||
	                    slidegrid.isUsed(usedCells, curCol + 1, curRow + 1) ||
	                    slidegrid.isUsed(usedCells, curCol, curRow + 1))) {
	                    /* 
	                     * If the current cell is used we
	                     * just want to try the next column. 
	                     * However, we also want to bump to 
	                     * the next row if needed so we are 
	                     * calling the column logic and then 
	                     * backing up the counter. 
	                     */
						i--;
	                } else {
	                    
	                    
	                    slidegrid.styleCell(children.eq(i), x, y, (cellWidth * 2) + padding, (cellHeight * 2) + padding, slidegrid.hasDrawn);
	                    /* 
	                     * Big cells are twice as large as normal cells.  That means they 
	                     * use up the cell to the right, the cell below, and the cell to 
	                     * the right and below. 
	                     */
	                    
	                    slidegrid.setUsed(usedCells, curCol, curRow); 
	                    slidegrid.setUsed(usedCells, curCol + 1, curRow);
	                    slidegrid.setUsed(usedCells, curCol, curRow + 1);  
	                    slidegrid.setUsed(usedCells, curCol + 1, curRow + 1); 
	                }
	                
	                hasTallCell = true;
	                
	            } else if (children.eq(i).hasClass("widecell")) {
					if (cols > 1 && (slidegrid.isUsed(usedCells, curCol, curRow) ||
	                    slidegrid.isUsed(usedCells, curCol + 1, curRow) ||
						curCol === cols - 1)) {
	                    /* 
	                     * If the current cell is used we
	                     * just want to try the next column. 
	                     * However, we also want to bump to 
	                     * the next row if needed so we are 
	                     * calling the column logic and then 
	                     * backing up the counter. 
	                     */
						i--;
	                    
	                } else {
	                    slidegrid.styleCell(children.eq(i), x, y, (cellWidth * 2) + padding, cellHeight, slidegrid.hasDrawn);
	                    /* 
	                     * Wide cells are twice as wide, but the same height
	                     */
	                    
	                    slidegrid.setUsed(usedCells, curCol, curRow); 
	                    slidegrid.setUsed(usedCells, curCol + 1, curRow);
	                }
	            } else if (children.eq(i).hasClass("tallcell")) {
	                if (slidegrid.isUsed(usedCells, curCol, curRow) ||
	                    slidegrid.isUsed(usedCells, curCol, curRow + 1)) {
	                    /* 
	                     * If the current cell is used we
	                     * just want to try the next column. 
	                     * However, we also want to bump to 
	                     * the next row if needed so we are 
	                     * calling the column logic and then 
	                     * backing up the counter. 
	                     */
						i--;
	                    
	                } else {
	                    slidegrid.styleCell(children.eq(i), x, y, cellWidth, (cellHeight * 2) + padding, slidegrid.hasDrawn);
	                    /* 
	                     * Tall cells are twice as tall, but the same width
	                     */
	                    
	                    slidegrid.setUsed(usedCells, curCol, curRow); 
	                    slidegrid.setUsed(usedCells, curCol, curRow + 1);
	                }
	                
	                hasTallCell = true;

	            } else {
	                if (slidegrid.isUsed(usedCells, curCol, curRow)) {
	                    /* 
	                     * If the current cell is used we
	                     * just want to try the next column. 
	                     * However, we also want to bump to 
	                     * the next row if needed so we are 
	                     * calling the column logic and then 
	                     * backing up the counter. 
	                     */
						i--;
	                    
	                } else {
	                    slidegrid.styleCell(children.eq(i), x, y, cellWidth, cellHeight, slidegrid.hasDrawn);
	                }
	            }
	            
	            if ((count % cols) === 0) {
	                /* 
	                 * This means it is time to bump down to the next row
	                 */
	                curCol = 0;
	                curRow++;
	                x = padding / 2;
	                y += cellHeight + padding;
					hasTallCell = false;
	            } else {
	                x += cellWidth + padding;
	                curCol++;
	            }
	            
	            count++;
	        }
			
			/*
	         * We need to set a height of the slidegrid div since it only has absolute 
	         * height tags within it.
	         */
	        var height = 0;
	        if ((count % cols) !== 1) {
	            height = y + cellHeight + padding;
	        } else {
	            height = y + padding;
	        }
	        
			if (hasTallCell) {
	            height += cellHeight + padding;
	        }

	        $(this).css('height', height + "px");
	        
	        
	        /*
	         * Now we reset the variables for the next grid.
	         */
	        x = padding / 2;
	        y = 0;
	        count = 1;
	    });
	    
	    slidegrid.hasDrawn = true;
	},
	
	inResize: false,
	
	resizeWindow: function(e) {
        if (!slidegrid.inResize && slidegrid.windowWidth != $(window).width()) {
			slidegrid.inResize = true;
			setTimeout(function() {
				slidegrid.alignGrid(slidegrid.cellWidth, slidegrid.cellHeight, slidegrid.padding, slidegrid.gridOffset);
				slidegrid.windowWidth = $(window).width();
				slidegrid.inResize = false;
			}, 500);
        }
    }
}

$(document).ready(function() {
    /*
     * When the user resizes the window we need to redraw the grid. 
     * This works well in Firefox and Chrome, but IE and Safari fire 
     * the resize events a little more often than they need to.  We 
     * remember the last window width so we can only repaint the grid 
     * when we need to. 
     */   
    $(window).bind("resize", slidegrid.resizeWindow);
});
