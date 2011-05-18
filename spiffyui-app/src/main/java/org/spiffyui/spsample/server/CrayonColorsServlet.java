/*******************************************************************************
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
package org.spiffyui.spsample.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This servlet gets colors that match the query string. It can be accessed as
 * GET /multivaluesuggestboxexample/colors. The colors are from
 * http://en.wikipedia.org/wiki/List_of_Crayola_crayon_colors
 */
public class CrayonColorsServlet extends HttpServlet
{
    private static final Logger LOGGER = Logger.getLogger(CrayonColorsServlet.class.getName());

    private static final long serialVersionUID = -3440848986748523758L;

    private static final List<String[]> COLOR_CODES;
    
    private String m_lastQuery;
    private JSONArray m_lastResults;
    
    static {
        COLOR_CODES = new ArrayList<String[]>();
        COLOR_CODES.add(new String[]{"Almond", "#EFDECD", "a brown issued in 1998", "(239, 222, 205)"});
        COLOR_CODES.add(new String[]{"Antique Brass", "#CD9575", "a metallic brown issued in 1998", "(205, 149, 117)"});
        COLOR_CODES.add(new String[]{"Apricot", "#FDD9B5", "an orange issued in 1949", "(253, 217, 181)"});
        COLOR_CODES.add(new String[]{"Aquamarine", "#78DBE2", "a blue issued in 1958", "(120, 219, 226)"});
        COLOR_CODES.add(new String[]{"Asparagus", "#87A96B", "a green issued in 1993", "(135, 169, 107)"});
        COLOR_CODES.add(new String[]{"Atomic Tangerine", "#FFA474", "a fluorescent orange renamed from 'Ultra Yellow' in 1990 issued in 1972", "(255, 164, 116)"});
        COLOR_CODES.add(new String[]{"Banana Mania", "#FAE7B5", "a yellow issued in 1998", "(250, 231, 181)"});
        COLOR_CODES.add(new String[]{"Beaver", "#9F8170", "a brown issued in 1998", "(159, 129, 112)"});
        COLOR_CODES.add(new String[]{"Bittersweet", "#FD7C6E", "an orange issued in 1949", "(253, 124, 110)"});
        COLOR_CODES.add(new String[]{"Black", "#000000", "a basic color issued in 1903", "(0,0,0)"});
        COLOR_CODES.add(new String[]{"Blizzard Blue", "#ACE5EE", "a fluorescent blue renamed from 'Ultra Blue' in 1990 issued in 1972 and retired in 2003", "(172, 229, 238)"});
        COLOR_CODES.add(new String[]{"Blue", "#1F75FE", "a basic color issued in 1903", "(31, 117, 254)"});
        COLOR_CODES.add(new String[]{"Blue Bell", "#A2A2D0", "a blue issued in 1998", "(162, 162, 208)"});
        COLOR_CODES.add(new String[]{"Blue Gray", "#6699CC", "a blue issued in 1958 and retired in 1990", "(102, 153, 204)"});
        COLOR_CODES.add(new String[]{"Blue Green", "#0D98BA", "a blue issued in 1949", "(13, 152, 186)"});
        COLOR_CODES.add(new String[]{"Blue Violet", "#7366BD", "a violet issued in 1949", "(115, 102, 189)"});
        COLOR_CODES.add(new String[]{"Blush", "#DE5D83", "a pink renamed from 'Cranberry' in 2000 issued in 1998", "(222, 93, 131)"});
        COLOR_CODES.add(new String[]{"Brick Red", "#CB4154", "a red issued in 1949", "(203, 65, 84)"});
        COLOR_CODES.add(new String[]{"Brown", "#B4674D", "a basic color issued in 1903", "(180, 103, 77)"});
        COLOR_CODES.add(new String[]{"Burnt Orange", "#FF7F49", "a dark orange issued in 1958", "(255, 127, 73)"});
        COLOR_CODES.add(new String[]{"Burnt Sienna", "#EA7E5D", "a brown issued in 1949", "(234, 126, 93)"});
        COLOR_CODES.add(new String[]{"Cadet Blue", "#B0B7C6", "a blue issued in 1958", "(176, 183, 198)"});
        COLOR_CODES.add(new String[]{"Canary", "#FFFF99", "a yellow issued in 1998", "(255, 255, 153)"});
        COLOR_CODES.add(new String[]{"Caribbean Green", "#1CD3A2", "a green issued in 1998", "(28, 211, 162)"});
        COLOR_CODES.add(new String[]{"Carnation Pink", "#FFAACC", "a pink issued in 1949", "(255, 170, 204)"});
        COLOR_CODES.add(new String[]{"Cerise", "#DD4492", "a pink issued in 1993", "(221, 68, 146)"});
        COLOR_CODES.add(new String[]{"Cerulean", "#1DACD6", "a blue issued in 1990", "(29, 172, 214)"});
        COLOR_CODES.add(new String[]{"Chestnut", "#BC5D58", "a brown issued renamed from 'Indian Red' in 1999 issued in 1958", "(188, 93, 88)"});
        COLOR_CODES.add(new String[]{"Copper", "#DD9475", "a metallic brown issued in 1958", "(221, 148, 117)"});
        COLOR_CODES.add(new String[]{"Cornflower", "#9ACEEB", "a blue issued in 1949", "(154, 206, 235)"});
        COLOR_CODES.add(new String[]{"Cotton Candy", "#FFBCD9", "a pink issued in 1998", "(255, 188, 217)"});
        COLOR_CODES.add(new String[]{"Dandelion", "#FDDB6D", "a yellow issued in 1990", "(253, 219, 109)"});
        COLOR_CODES.add(new String[]{"Denim", "#2B6CC4", "a blue issued in 1993", "(43, 108, 196)"});
        COLOR_CODES.add(new String[]{"Desert Sand", "#EFCDB8", "a brown issued in 1998", "(239, 205, 184)"});
        COLOR_CODES.add(new String[]{"Eggplant", "#6E5160", "a violet issued in 1998", "(110, 81, 96)"});
        COLOR_CODES.add(new String[]{"Electric Lime", "#CEFF1D", "a fluorescent green issued in 1990", "(206, 255, 29)"});
        COLOR_CODES.add(new String[]{"Fern", "#71BC78", "a green issued in 1998", "(113, 188, 120)"});
        COLOR_CODES.add(new String[]{"Forest Green", "#6DAE81", "a green issued in 1958", "(109, 174, 129)"});
        COLOR_CODES.add(new String[]{"Fuchsia", "#C364C5", "a pink issued in 1990", "(195, 100, 197)"});
        COLOR_CODES.add(new String[]{"Fuzzy Wuzzy", "#CC6666", "a brown issued in 1998", "(204, 102, 102)"});
        COLOR_CODES.add(new String[]{"Gold", "#E7C697", "a metallic yellow issued in 1949", "(231, 198, 151)"});
        COLOR_CODES.add(new String[]{"Goldenrod", "#FCD975", "a yellow issued in 1958", "(252, 217, 117)"});
        COLOR_CODES.add(new String[]{"Granny Smith Apple", "#A8E4A0", "a green issued in 1993", "(168, 228, 160)"});
        COLOR_CODES.add(new String[]{"Gray", "#95918C", "a light black issued in 1949", "(149, 145, 140)"});
        COLOR_CODES.add(new String[]{"Green", "#1CAC78", "a base color issued in 1903", "(28, 172, 120)"});
        COLOR_CODES.add(new String[]{"Green Blue", "#1164B4", "a blue issued in 1949 and retired in 1990", "(17, 100, 180)"});
        COLOR_CODES.add(new String[]{"Green Yellow", "#F0E891", "a yellow issued in 1949", "(240, 232, 145)"});
        COLOR_CODES.add(new String[]{"Hot Magenta", "#FF1DCE", "a fluorescent pink", "(255, 29, 206)"});
        COLOR_CODES.add(new String[]{"Inchworm", "#B2EC5D", "a green issued in 2003", "(178, 236, 93)"});
        COLOR_CODES.add(new String[]{"Indigo", "#5D76CB", "a blue issued in 2000", "(93, 118, 203)"});
        COLOR_CODES.add(new String[]{"Jazzberry Jam", "#CA3767", "a red issued in 2003", "(202, 55, 103)"});
        COLOR_CODES.add(new String[]{"Jungle Green", "#3BB08F", "a green issued in 1990", "(59, 176, 143)"});
        COLOR_CODES.add(new String[]{"Laser Lemon", "#FEFE22", "a fluorescent yellow renamed from 'Chartreuse' in 1990 issued in 1972", "(254, 254, 34)"});
        COLOR_CODES.add(new String[]{"Lavender", "#FCB4D5", "a violet issued 1958", "(252, 180, 213)"});
        COLOR_CODES.add(new String[]{"Lemon Yellow", "#FFF44F", "a yellow issued in 1949 and retired in 1990", "(255, 244, 79)"});
        COLOR_CODES.add(new String[]{"Macaroni and Cheese", "#FFBD88", "an orange issued in 1993", "(255, 189, 136)"});
        COLOR_CODES.add(new String[]{"Magenta", "#F664AF", "a red issued in 1949", "(246, 100, 175)"});
        COLOR_CODES.add(new String[]{"Magic Mint", "#AAF0D1", "a fluorescent green issued in 1990 and retired in 2003", "(170, 240, 209)"});
        COLOR_CODES.add(new String[]{"Mahogany", "#CD4A4C", "a brown issued in 1949", "(205, 74, 76)"});
        COLOR_CODES.add(new String[]{"Maize", "#EDD19C", "a yellow issued in 1949 and retired in 1990", "(237, 209, 156)"});
        COLOR_CODES.add(new String[]{"Manatee", "#979AAA", "a violet issued in 1998", "(151, 154, 170)"});
        COLOR_CODES.add(new String[]{"Mango Tango", "#FF8243", "an orange issued in 2003", "(255, 130, 67)"});
        COLOR_CODES.add(new String[]{"Maroon", "#C8385A", "a red issued in 1949", "(200, 56, 90)"});
        COLOR_CODES.add(new String[]{"Mauvelous", "#EF98AA", "a violet issued in 1993", "(239, 152, 170)"});
        COLOR_CODES.add(new String[]{"Melon", "#FDBCB4", "an orange issued in 1949", "(253, 188, 180)"});
        COLOR_CODES.add(new String[]{"Midnight Blue", "#1A4876", "a blue renamed from 'Prussian Blue' in 1958 issued in 1949", "(26, 72, 118)"});
        COLOR_CODES.add(new String[]{"Mountain Meadow", "#30BA8F", "a green issued in 1998", "(48, 186, 143)"});
        COLOR_CODES.add(new String[]{"Mulberry", "#C54B8C", "a violet issued in 1958 and retired in 2003", "(197, 75, 140)"});
        COLOR_CODES.add(new String[]{"Navy Blue", "#1974D2", "a blue issued in 1958", "(25, 116, 210)"});
        COLOR_CODES.add(new String[]{"Neon Carrot", "#FFA343", "a fluorescent orange issued in 1990", "(255, 163, 67)"});
        COLOR_CODES.add(new String[]{"Olive Green", "#BAB86C", "a green issued in 1949", "(186, 184, 108)"});
        COLOR_CODES.add(new String[]{"Orange", "#FF7538", "a base color", "(255, 117, 56)"});
        COLOR_CODES.add(new String[]{"Orange Red", "#FF2B2B", "a red issued in 1949 and retired in 1990", "(255, 43, 43)"});
        COLOR_CODES.add(new String[]{"Orange Yellow", "#F8D568", "a yellow issued in 1949 and retired in 1990", "(248, 213, 104)"});
        COLOR_CODES.add(new String[]{"Orchid", "#E6A8D7", "a pink issued in 1949", "(230, 168, 215)"});
        COLOR_CODES.add(new String[]{"Outer Space", "#414A4C", "a lighter black issued in 1998", "(65, 74, 76)"});
        COLOR_CODES.add(new String[]{"Outrageous Orange", "#FF6E4A", "a fluorescent orange renamed from 'Ultra Orange' in 1990 issued in 1972", "(255, 110, 74)"});
        COLOR_CODES.add(new String[]{"Pacific Blue", "#1CA9C9", "a blue issued in 1993", "(28, 169, 201)"});
        COLOR_CODES.add(new String[]{"Peach", "#FFCFAB", "an orange renamed from 'Flesh' in 1962 issued in 1949", "(255, 207, 171)"});
        COLOR_CODES.add(new String[]{"Periwinkle", "#C5D0E6", "a blue issued in 1949", "(197, 208, 230)"});
        COLOR_CODES.add(new String[]{"Piggy Pink", "#FDDDE6", "a pink issued in 1998", "(253, 221, 230)"});
        COLOR_CODES.add(new String[]{"Pine Green", "#158078", "a green issued in 1949", "(21, 128, 120)"});
        COLOR_CODES.add(new String[]{"Pink Flamingo", "#FC74FD", "a pink issued in 1998", "(252, 116, 253)"});
        COLOR_CODES.add(new String[]{"Pink Sherbert", "#F78FA7", "a pink renamed from 'Brink Pink' in 2000 issued in 1998", "(247, 143, 167)"});
        COLOR_CODES.add(new String[]{"Plum", "#8E4585", "a violet issued in 1958", "(142, 69, 133)"});
        COLOR_CODES.add(new String[]{"Purple Heart", "#7442C8", "a violet issued in 1998", "(116, 66, 200)"});
        COLOR_CODES.add(new String[]{"Purple Mountain's Majesty", "#9D81BA", "a violet issued in 1998", "(157, 129, 186)"});
        COLOR_CODES.add(new String[]{"Purple Pizzazz", "#FE4EDA", "a fluorescent violet issued in 1990", "(254, 78, 218)"});
        COLOR_CODES.add(new String[]{"Radical Red", "#FF496C", "a fluorescent red issued in 1990", "(255, 73, 108)"});
        COLOR_CODES.add(new String[]{"Raw Sienna", "#D68A59", "a brown issued in 1958", "(214, 138, 89)"});
        COLOR_CODES.add(new String[]{"Raw Umber", "#714B23", "a brown issued in 1958 retired in 1990", "(113, 75, 35)"});
        COLOR_CODES.add(new String[]{"Razzle Dazzle Rose", "#FF48D0", "a fluorescent red issued in 1990", "(255, 72, 208)"});
        COLOR_CODES.add(new String[]{"Razzmatazz", "#E3256B", "a red issued in 1993", "(227, 37, 107)"});
        COLOR_CODES.add(new String[]{"Red", "#EE204D", "a base color", "(238, 32, 77)"});
        COLOR_CODES.add(new String[]{"Red Orange", "#FF5349", "an orange issued in 1949", "(255, 83, 73)"});
        COLOR_CODES.add(new String[]{"Red Violet", "#C0448F", "a violet issued in 1949", "(192, 68, 143)"});
        COLOR_CODES.add(new String[]{"Robin's Egg Blue", "#1FCECB", "a blue issued in 1993", "(31, 206, 203)"});
        COLOR_CODES.add(new String[]{"Royal Purple", "#7851A9", "a violet issued in 1990", "(120, 81, 169)"});
        COLOR_CODES.add(new String[]{"Salmon", "#FF9BAA", "a pink issued in 1949", "(255, 155, 170)"});
        COLOR_CODES.add(new String[]{"Scarlet", "#FC2847", "a red renamed from 'Torch Red' in 2000 issued in 1998", "(252, 40, 71)"});
        COLOR_CODES.add(new String[]{"Screamin' Green", "#76FF7A", "a fluorescent green renamed from 'Ultra Green' in 1990 issued in 1972", "(118, 255, 122)"});
        COLOR_CODES.add(new String[]{"Sea Green", "#9FE2BF", "a green issued in 1949", "(159, 226, 191)"});
        COLOR_CODES.add(new String[]{"Sepia", "#A5694F", "a brown issued in 1958", "(165, 105, 79)"});
        COLOR_CODES.add(new String[]{"Shadow", "#8A795D", "a lighter black issued in 1998", "(138, 121, 93)"});
        COLOR_CODES.add(new String[]{"Shamrock", "#45CEA2", "a green issued in 1993", "(69, 206, 162)"});
        COLOR_CODES.add(new String[]{"Shocking Pink", "#FB7EFD", "a fluorescent pink renamed from 'Ultra Pink' in 1990 issued in 1972", "(251, 126, 253)"});
        COLOR_CODES.add(new String[]{"Silver", "#CDC5C2", "a metallic light black issued in 1949", "(205, 197, 194)"});
        COLOR_CODES.add(new String[]{"Sky Blue", "#80DAEB", "a blue issued in 1958", "(128, 218, 235)"});
        COLOR_CODES.add(new String[]{"Spring Green", "#ECEABE", "a green issued in 1949", "(236, 234, 190)"});
        COLOR_CODES.add(new String[]{"Sunglow", "#FFCF48", "a fluorescent yellow issued in 1990", "(255, 207, 72)"});
        COLOR_CODES.add(new String[]{"Sunset Orange", "#FD5E53", "an orange issued in 1998", "(253, 94, 83)"});
        COLOR_CODES.add(new String[]{"Tan", "#FAA76C", "a brown issued in 1949", "(250, 167, 108)"});
        COLOR_CODES.add(new String[]{"Teal Blue", "#18A7B5", "a blue issued in 1990 and retired in 2003", "(24, 167, 181)"});
        COLOR_CODES.add(new String[]{"Thistle", "#EBC7DF", "a pink issued in 1949 and retired in 2000", "(235, 199, 223)"});
        COLOR_CODES.add(new String[]{"Tickle Me Pink", "#FC89AC", "a pink named ina contest by ClevverTV Personality Josyln Davis issued in 1993", "(252, 137, 172)"});
        COLOR_CODES.add(new String[]{"Timberwolf", "#DBD7D2", "a light black issued in 1993", "(219, 215, 210)"});
        COLOR_CODES.add(new String[]{"Tropical Rain Forest", "#17806D", "a green issued in 1993", "(23, 128, 109)"});
        COLOR_CODES.add(new String[]{"Tumbleweed", "#DEAA88", "a brown issued in 1993", "(222, 170, 136)"});
        COLOR_CODES.add(new String[]{"Turquoise Blue", "#77DDE7", "a blue issued in 1949", "(119, 221, 231)"});
        COLOR_CODES.add(new String[]{"Unmellow Yellow", "#FFFF66", "a fluorescent yellow issued in 1990", "(255, 255, 102)"});
        COLOR_CODES.add(new String[]{"Violet (Purple)", "#926EAE", "a base color issued in 1903", "(146, 110, 174)"});
        COLOR_CODES.add(new String[]{"Violet Blue", "#324AB2", "a blue issued in 1949 and retired in 1990", "(50, 74, 178)"});
        COLOR_CODES.add(new String[]{"Violet Red", "#F75394", "a red issued in 1949", "(247, 83, 148)"});
        COLOR_CODES.add(new String[]{"Vivid Tangerine", "#FFA089", "an orange issued in 1990", "(255, 160, 137)"});
        COLOR_CODES.add(new String[]{"Vivid Violet", "#8F509D", "a violet issued in 1998", "(143, 80, 157)"});
        COLOR_CODES.add(new String[]{"White", "#FFFFFF", "a base color issued in 1949", "(255, 255, 255)"});
        COLOR_CODES.add(new String[]{"Wild Blue Yonder", "#A2ADD0", "a blue issued in 2003", "(162, 173, 208)"});
        COLOR_CODES.add(new String[]{"Wild Strawberry", "#FF43A4", "a red issued in 1990", "(255, 67, 164)"});
        COLOR_CODES.add(new String[]{"Wild Watermelon", "#FC6C85", "a fluorescent red renamed from 'Ultra Red' in 1990 issued in 1972", "(252, 108, 133)"});
        COLOR_CODES.add(new String[]{"Wisteria", "#CDA4DE", "a violet issued in 1993", "(205, 164, 222)"});
        COLOR_CODES.add(new String[]{"Yellow", "#FCE883", "a base color issued in 1903", "(252, 232, 131)"});
        COLOR_CODES.add(new String[]{"Yellow Green", "#C5E384", "a green issued in 1949", "(197, 227, 132)"});
        COLOR_CODES.add(new String[]{"Yellow Orange", "#FFAE42", "an orange issued in 1949", "(255, 174, 66)"});

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException,
            IOException
    {
        JSONArray fullColorArray;
        String query = request.getParameter("q");
        
        try {
            int count = 0;
            if (query.equals(m_lastQuery)) {
                fullColorArray = m_lastResults;
                count = m_lastResults.length();
            } else {
                m_lastQuery = query;
                
                fullColorArray = new JSONArray();
                for (String[] colorCode : COLOR_CODES) {
                    String colorName = colorCode[0];
                    String lowerColor = colorName.toLowerCase();
                    int has = lowerColor.indexOf(query.toLowerCase());
                    
                    if (!query.isEmpty() && (query.equals("*") || has >= 0)) {
                        JSONObject color = new JSONObject();
                        color.put("DisplayName", colorName);
                        color.put("Value", colorCode[1]);
                        color.put("Description", colorCode[2]);
                        color.put("RGB", colorCode[3]);
                        fullColorArray.put(color);
                        count++;
                    }
                }
                m_lastResults = fullColorArray;
            }
            
            //get the partial array to be returned
            int indexFrom = Integer.parseInt(request.getParameter("indexFrom"));
            int indexTo = Integer.parseInt(request.getParameter("indexTo"));
            
            JSONArray partial = new JSONArray();
            if (fullColorArray.length() > 0) {
                int end = count - 1 > indexTo ? indexTo : count - 1;
                for (int i = indexFrom; i <= end; i++) {
                    partial.put(fullColorArray.get(i));
                }
            }        
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            
            JSONObject obj = new JSONObject();
            obj.put("TotalSize", count);
            obj.put("Options", partial);
            out.println(obj.toString());
        } catch (JSONException e) {
            /*
             This should never happen
             */
            LOGGER.throwing(getClass().getName(), "doGet", e);
        }
    }
}
