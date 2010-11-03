/*
 * ========================================================================
 *
 * Copyright (c) 2005 Unpublished Work of Novell, Inc. All Rights Reserved.
 * 
 * THIS WORK IS AN UNPUBLISHED WORK AND CONTAINS CONFIDENTIAL,
 * PROPRIETARY AND TRADE SECRET INFORMATION OF NOVELL, INC. ACCESS TO
 * THIS WORK IS RESTRICTED TO (I) NOVELL, INC. EMPLOYEES WHO HAVE A NEED
 * TO KNOW HOW TO PERFORM TASKS WITHIN THE SCOPE OF THEIR ASSIGNMENTS AND
 * (II) ENTITIES OTHER THAN NOVELL, INC. WHO HAVE ENTERED INTO
 * APPROPRIATE LICENSE AGREEMENTS. NO PART OF THIS WORK MAY BE USED,
 * PRACTICED, PERFORMED, COPIED, DISTRIBUTED, REVISED, MODIFIED,
 * TRANSLATED, ABRIDGED, CONDENSED, EXPANDED, COLLECTED, COMPILED,
 * LINKED, RECAST, TRANSFORMED OR ADAPTED WITHOUT THE PRIOR WRITTEN
 * CONSENT OF NOVELL, INC. ANY USE OR EXPLOITATION OF THIS WORK WITHOUT
 * AUTHORIZATION COULD SUBJECT THE PERPETRATOR TO CRIMINAL AND CIVIL
 * LIABILITY.
 *
 * ========================================================================
 */
package org.spiffyui.spsample.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This servlet gets colors that match the query string. It can be accessed as
 * GET /multivaluesuggestboxexample/colors. The colors are from
 * http://en.wikipedia.org/wiki/List_of_Crayola_crayon_colors
 */
public class CrayonColorsServlet extends HttpServlet
{

    private static final long serialVersionUID = -3440848986748523758L;

    private static final List<String[]> COLOR_CODES;
    
    private String m_lastQuery;
    private JSONArray m_lastResults;
    
    static {
        COLOR_CODES = new ArrayList<String[]>();
        COLOR_CODES.add(new String[]{"Almond", "#EFDECD"});
        COLOR_CODES.add(new String[]{"Antique Brass", "#CD9575"});
        COLOR_CODES.add(new String[]{"Apricot", "#FDD9B5"});
        COLOR_CODES.add(new String[]{"Aquamarine", "#78DBE2"});
        COLOR_CODES.add(new String[]{"Asparagus", "#87A96B"});
        COLOR_CODES.add(new String[]{"Atomic Tangerine", "#FFA474"});
        COLOR_CODES.add(new String[]{"Banana Mania", "#FAE7B5"});
        COLOR_CODES.add(new String[]{"Beaver", "#9F8170"});
        COLOR_CODES.add(new String[]{"Bittersweet", "#FD7C6E"});
        COLOR_CODES.add(new String[]{"Black", "#000000"});
        COLOR_CODES.add(new String[]{"Blizzard Blue", "#ACE5EE"});
        COLOR_CODES.add(new String[]{"Blue", "#1F75FE"});
        COLOR_CODES.add(new String[]{"Blue Bell", "#A2A2D0"});
        COLOR_CODES.add(new String[]{"Blue Gray", "#6699CC"});
        COLOR_CODES.add(new String[]{"Blue Green", "#0D98BA"});
        COLOR_CODES.add(new String[]{"Blue Violet", "#7366BD"});
        COLOR_CODES.add(new String[]{"Blush", "#DE5D83"});
        COLOR_CODES.add(new String[]{"Brick Red", "#CB4154"});
        COLOR_CODES.add(new String[]{"Brown", "#B4674D"});
        COLOR_CODES.add(new String[]{"Burnt Orange", "#FF7F49"});
        COLOR_CODES.add(new String[]{"Burnt Sienna", "#EA7E5D"});
        COLOR_CODES.add(new String[]{"Cadet Blue", "#B0B7C6"});
        COLOR_CODES.add(new String[]{"Canary", "#FFFF99"});
        COLOR_CODES.add(new String[]{"Caribbean Green", "#1CD3A2"});
        COLOR_CODES.add(new String[]{"Carnation Pink", "#FFAACC"});
        COLOR_CODES.add(new String[]{"Cerise", "#DD4492"});
        COLOR_CODES.add(new String[]{"Cerulean", "#1DACD6"});
        COLOR_CODES.add(new String[]{"Chestnut", "#BC5D58"});
        COLOR_CODES.add(new String[]{"Copper", "#DD9475"});
        COLOR_CODES.add(new String[]{"Cornflower", "#9ACEEB"});
        COLOR_CODES.add(new String[]{"Cotton Candy", "#FFBCD9"});
        COLOR_CODES.add(new String[]{"Dandelion", "#FDDB6D"});
        COLOR_CODES.add(new String[]{"Denim", "#2B6CC4"});
        COLOR_CODES.add(new String[]{"Desert Sand", "#EFCDB8"});
        COLOR_CODES.add(new String[]{"Eggplant", "#6E5160"});
        COLOR_CODES.add(new String[]{"Electric Lime", "#CEFF1D"});
        COLOR_CODES.add(new String[]{"Fern", "#71BC78"});
        COLOR_CODES.add(new String[]{"Forest Green", "#6DAE81"});
        COLOR_CODES.add(new String[]{"Fuchsia", "#C364C5"});
        COLOR_CODES.add(new String[]{"Fuzzy Wuzzy", "#CC6666"});
        COLOR_CODES.add(new String[]{"Gold", "#E7C697"});
        COLOR_CODES.add(new String[]{"Goldenrod", "#FCD975"});
        COLOR_CODES.add(new String[]{"Granny Smith Apple", "#A8E4A0"});
        COLOR_CODES.add(new String[]{"Gray", "#95918C"});
        COLOR_CODES.add(new String[]{"Green", "#1CAC78"});
        COLOR_CODES.add(new String[]{"Green Blue", "#1164B4"});
        COLOR_CODES.add(new String[]{"Green Yellow", "#F0E891"});
        COLOR_CODES.add(new String[]{"Hot Magenta", "#FF1DCE"});
        COLOR_CODES.add(new String[]{"Inchworm", "#B2EC5D"});
        COLOR_CODES.add(new String[]{"Indigo", "#5D76CB"});
        COLOR_CODES.add(new String[]{"Jazzberry Jam", "#CA3767"});
        COLOR_CODES.add(new String[]{"Jungle Green", "#3BB08F"});
        COLOR_CODES.add(new String[]{"Laser Lemon", "#FEFE22"});
        COLOR_CODES.add(new String[]{"Lavender", "#FCB4D5"});
        COLOR_CODES.add(new String[]{"Lemon Yellow", "#FFF44F"});
        COLOR_CODES.add(new String[]{"Macaroni and Cheese", "#FFBD88"});
        COLOR_CODES.add(new String[]{"Magenta", "#F664AF"});
        COLOR_CODES.add(new String[]{"Magic Mint", "#AAF0D1"});
        COLOR_CODES.add(new String[]{"Mahogany", "#CD4A4C"});
        COLOR_CODES.add(new String[]{"Maize", "#EDD19C"});
        COLOR_CODES.add(new String[]{"Manatee", "#979AAA"});
        COLOR_CODES.add(new String[]{"Mango Tango", "#FF8243"});
        COLOR_CODES.add(new String[]{"Maroon", "#C8385A"});
        COLOR_CODES.add(new String[]{"Mauvelous", "#EF98AA"});
        COLOR_CODES.add(new String[]{"Melon", "#FDBCB4"});
        COLOR_CODES.add(new String[]{"Midnight Blue", "#1A4876"});
        COLOR_CODES.add(new String[]{"Mountain Meadow", "#30BA8F"});
        COLOR_CODES.add(new String[]{"Mulberry", "#C54B8C"});
        COLOR_CODES.add(new String[]{"Navy Blue", "#1974D2"});
        COLOR_CODES.add(new String[]{"Neon Carrot", "#FFA343"});
        COLOR_CODES.add(new String[]{"Olive Green", "#BAB86C"});
        COLOR_CODES.add(new String[]{"Orange", "#FF7538"});
        COLOR_CODES.add(new String[]{"Orange Red", "#FF2B2B"});
        COLOR_CODES.add(new String[]{"Orange Yellow", "#F8D568"});
        COLOR_CODES.add(new String[]{"Orchid", "#E6A8D7"});
        COLOR_CODES.add(new String[]{"Outer Space", "#414A4C"});
        COLOR_CODES.add(new String[]{"Outrageous Orange", "#FF6E4A"});
        COLOR_CODES.add(new String[]{"Pacific Blue", "#1CA9C9"});
        COLOR_CODES.add(new String[]{"Peach", "#FFCFAB"});
        COLOR_CODES.add(new String[]{"Periwinkle", "#C5D0E6"});
        COLOR_CODES.add(new String[]{"Piggy Pink", "#FDDDE6"});
        COLOR_CODES.add(new String[]{"Pine Green", "#158078"});
        COLOR_CODES.add(new String[]{"Pink Flamingo", "#FC74FD"});
        COLOR_CODES.add(new String[]{"Pink Sherbert", "#F78FA7"});
        COLOR_CODES.add(new String[]{"Plum", "#8E4585"});
        COLOR_CODES.add(new String[]{"Purple Heart", "#7442C8"});
        COLOR_CODES.add(new String[]{"Purple Mountain's Majesty", "#9D81BA"});
        COLOR_CODES.add(new String[]{"Purple Pizzazz", "#FE4EDA"});
        COLOR_CODES.add(new String[]{"Radical Red", "#FF496C"});
        COLOR_CODES.add(new String[]{"Raw Sienna", "#D68A59"});
        COLOR_CODES.add(new String[]{"Raw Umber", "#714B23"});
        COLOR_CODES.add(new String[]{"Razzle Dazzle Rose", "#FF48D0"});
        COLOR_CODES.add(new String[]{"Razzmatazz", "#E3256B"});
        COLOR_CODES.add(new String[]{"Red", "#EE204D"});
        COLOR_CODES.add(new String[]{"Red Orange", "#FF5349"});
        COLOR_CODES.add(new String[]{"Red Violet", "#C0448F"});
        COLOR_CODES.add(new String[]{"Robin's Egg Blue", "#1FCECB"});
        COLOR_CODES.add(new String[]{"Royal Purple", "#7851A9"});
        COLOR_CODES.add(new String[]{"Salmon", "#FF9BAA"});
        COLOR_CODES.add(new String[]{"Scarlet", "#FC2847"});
        COLOR_CODES.add(new String[]{"Screamin' Green", "#76FF7A"});
        COLOR_CODES.add(new String[]{"Sea Green", "#9FE2BF"});
        COLOR_CODES.add(new String[]{"Sepia", "#A5694F"});
        COLOR_CODES.add(new String[]{"Shadow", "#8A795D"});
        COLOR_CODES.add(new String[]{"Shamrock", "#45CEA2"});
        COLOR_CODES.add(new String[]{"Shocking Pink", "#FB7EFD"});
        COLOR_CODES.add(new String[]{"Silver", "#CDC5C2"});
        COLOR_CODES.add(new String[]{"Sky Blue", "#80DAEB"});
        COLOR_CODES.add(new String[]{"Spring Green", "#ECEABE"});
        COLOR_CODES.add(new String[]{"Sunglow", "#FFCF48"});
        COLOR_CODES.add(new String[]{"Sunset Orange", "#FD5E53"});
        COLOR_CODES.add(new String[]{"Tan", "#FAA76C"});
        COLOR_CODES.add(new String[]{"Teal Blue", "#18A7B5"});
        COLOR_CODES.add(new String[]{"Thistle", "#EBC7DF"});
        COLOR_CODES.add(new String[]{"Tickle Me Pink", "#FC89AC"});
        COLOR_CODES.add(new String[]{"Timberwolf", "#DBD7D2"});
        COLOR_CODES.add(new String[]{"Tropical Rain Forest", "#17806D"});
        COLOR_CODES.add(new String[]{"Tumbleweed", "#DEAA88"});
        COLOR_CODES.add(new String[]{"Turquoise Blue", "#77DDE7"});
        COLOR_CODES.add(new String[]{"Unmellow Yellow", "#FFFF66"});
        COLOR_CODES.add(new String[]{"Violet (Purple)", "#926EAE"});
        COLOR_CODES.add(new String[]{"Violet Blue", "#324AB2"});
        COLOR_CODES.add(new String[]{"Violet Red", "#F75394"});
        COLOR_CODES.add(new String[]{"Vivid Tangerine", "#FFA089"});
        COLOR_CODES.add(new String[]{"Vivid Violet", "#8F509D"});
        COLOR_CODES.add(new String[]{"White", "#FFFFFF"});
        COLOR_CODES.add(new String[]{"Wild Blue Yonder", "#A2ADD0"});
        COLOR_CODES.add(new String[]{"Wild Strawberry", "#FF43A4"});
        COLOR_CODES.add(new String[]{"Wild Watermelon", "#FC6C85"});
        COLOR_CODES.add(new String[]{"Wisteria", "#CDA4DE"});
        COLOR_CODES.add(new String[]{"Yellow", "#FCE883"});
        COLOR_CODES.add(new String[]{"Yellow Green", "#C5E384"});
        COLOR_CODES.add(new String[]{"Yellow Orange", "#FFAE42"});

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException,
            IOException
    {
        JSONArray fullColorArray;
        String query = request.getParameter("q");
        
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
    }
}
