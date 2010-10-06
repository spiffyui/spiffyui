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
package com.novell.spsample.server;

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

    private static final List<String[]> g_colorCodes;
    
    private String m_lastQuery;
    private JSONArray m_lastResults;
    
    static {
        g_colorCodes = new ArrayList<String[]>();
        g_colorCodes.add(new String[]{"Almond", "#EFDECD"});
        g_colorCodes.add(new String[]{"Antique Brass", "#CD9575"});
        g_colorCodes.add(new String[]{"Apricot", "#FDD9B5"});
        g_colorCodes.add(new String[]{"Aquamarine", "#78DBE2"});
        g_colorCodes.add(new String[]{"Asparagus", "#87A96B"});
        g_colorCodes.add(new String[]{"Atomic Tangerine", "#FFA474"});
        g_colorCodes.add(new String[]{"Banana Mania", "#FAE7B5"});
        g_colorCodes.add(new String[]{"Beaver", "#9F8170"});
        g_colorCodes.add(new String[]{"Bittersweet", "#FD7C6E"});
        g_colorCodes.add(new String[]{"Black", "#000000"});
        g_colorCodes.add(new String[]{"Blizzard Blue", "#ACE5EE"});
        g_colorCodes.add(new String[]{"Blue", "#1F75FE"});
        g_colorCodes.add(new String[]{"Blue Bell", "#A2A2D0"});
        g_colorCodes.add(new String[]{"Blue Gray", "#6699CC"});
        g_colorCodes.add(new String[]{"Blue Green", "#0D98BA"});
        g_colorCodes.add(new String[]{"Blue Violet", "#7366BD"});
        g_colorCodes.add(new String[]{"Blush", "#DE5D83"});
        g_colorCodes.add(new String[]{"Brick Red", "#CB4154"});
        g_colorCodes.add(new String[]{"Brown", "#B4674D"});
        g_colorCodes.add(new String[]{"Burnt Orange", "#FF7F49"});
        g_colorCodes.add(new String[]{"Burnt Sienna", "#EA7E5D"});
        g_colorCodes.add(new String[]{"Cadet Blue", "#B0B7C6"});
        g_colorCodes.add(new String[]{"Canary", "#FFFF99"});
        g_colorCodes.add(new String[]{"Caribbean Green", "#1CD3A2"});
        g_colorCodes.add(new String[]{"Carnation Pink", "#FFAACC"});
        g_colorCodes.add(new String[]{"Cerise", "#DD4492"});
        g_colorCodes.add(new String[]{"Cerulean", "#1DACD6"});
        g_colorCodes.add(new String[]{"Chestnut", "#BC5D58"});
        g_colorCodes.add(new String[]{"Copper", "#DD9475"});
        g_colorCodes.add(new String[]{"Cornflower", "#9ACEEB"});
        g_colorCodes.add(new String[]{"Cotton Candy", "#FFBCD9"});
        g_colorCodes.add(new String[]{"Dandelion", "#FDDB6D"});
        g_colorCodes.add(new String[]{"Denim", "#2B6CC4"});
        g_colorCodes.add(new String[]{"Desert Sand", "#EFCDB8"});
        g_colorCodes.add(new String[]{"Eggplant", "#6E5160"});
        g_colorCodes.add(new String[]{"Electric Lime", "#CEFF1D"});
        g_colorCodes.add(new String[]{"Fern", "#71BC78"});
        g_colorCodes.add(new String[]{"Forest Green", "#6DAE81"});
        g_colorCodes.add(new String[]{"Fuchsia", "#C364C5"});
        g_colorCodes.add(new String[]{"Fuzzy Wuzzy", "#CC6666"});
        g_colorCodes.add(new String[]{"Gold", "#E7C697"});
        g_colorCodes.add(new String[]{"Goldenrod", "#FCD975"});
        g_colorCodes.add(new String[]{"Granny Smith Apple", "#A8E4A0"});
        g_colorCodes.add(new String[]{"Gray", "#95918C"});
        g_colorCodes.add(new String[]{"Green", "#1CAC78"});
        g_colorCodes.add(new String[]{"Green Blue", "#1164B4"});
        g_colorCodes.add(new String[]{"Green Yellow", "#F0E891"});
        g_colorCodes.add(new String[]{"Hot Magenta", "#FF1DCE"});
        g_colorCodes.add(new String[]{"Inchworm", "#B2EC5D"});
        g_colorCodes.add(new String[]{"Indigo", "#5D76CB"});
        g_colorCodes.add(new String[]{"Jazzberry Jam", "#CA3767"});
        g_colorCodes.add(new String[]{"Jungle Green", "#3BB08F"});
        g_colorCodes.add(new String[]{"Laser Lemon", "#FEFE22"});
        g_colorCodes.add(new String[]{"Lavender", "#FCB4D5"});
        g_colorCodes.add(new String[]{"Lemon Yellow", "#FFF44F"});
        g_colorCodes.add(new String[]{"Macaroni and Cheese", "#FFBD88"});
        g_colorCodes.add(new String[]{"Magenta", "#F664AF"});
        g_colorCodes.add(new String[]{"Magic Mint", "#AAF0D1"});
        g_colorCodes.add(new String[]{"Mahogany", "#CD4A4C"});
        g_colorCodes.add(new String[]{"Maize", "#EDD19C"});
        g_colorCodes.add(new String[]{"Manatee", "#979AAA"});
        g_colorCodes.add(new String[]{"Mango Tango", "#FF8243"});
        g_colorCodes.add(new String[]{"Maroon", "#C8385A"});
        g_colorCodes.add(new String[]{"Mauvelous", "#EF98AA"});
        g_colorCodes.add(new String[]{"Melon", "#FDBCB4"});
        g_colorCodes.add(new String[]{"Midnight Blue", "#1A4876"});
        g_colorCodes.add(new String[]{"Mountain Meadow", "#30BA8F"});
        g_colorCodes.add(new String[]{"Mulberry", "#C54B8C"});
        g_colorCodes.add(new String[]{"Navy Blue", "#1974D2"});
        g_colorCodes.add(new String[]{"Neon Carrot", "#FFA343"});
        g_colorCodes.add(new String[]{"Olive Green", "#BAB86C"});
        g_colorCodes.add(new String[]{"Orange", "#FF7538"});
        g_colorCodes.add(new String[]{"Orange Red", "#FF2B2B"});
        g_colorCodes.add(new String[]{"Orange Yellow", "#F8D568"});
        g_colorCodes.add(new String[]{"Orchid", "#E6A8D7"});
        g_colorCodes.add(new String[]{"Outer Space", "#414A4C"});
        g_colorCodes.add(new String[]{"Outrageous Orange", "#FF6E4A"});
        g_colorCodes.add(new String[]{"Pacific Blue", "#1CA9C9"});
        g_colorCodes.add(new String[]{"Peach", "#FFCFAB"});
        g_colorCodes.add(new String[]{"Periwinkle", "#C5D0E6"});
        g_colorCodes.add(new String[]{"Piggy Pink", "#FDDDE6"});
        g_colorCodes.add(new String[]{"Pine Green", "#158078"});
        g_colorCodes.add(new String[]{"Pink Flamingo", "#FC74FD"});
        g_colorCodes.add(new String[]{"Pink Sherbert", "#F78FA7"});
        g_colorCodes.add(new String[]{"Plum", "#8E4585"});
        g_colorCodes.add(new String[]{"Purple Heart", "#7442C8"});
        g_colorCodes.add(new String[]{"Purple Mountain's Majesty", "#9D81BA"});
        g_colorCodes.add(new String[]{"Purple Pizzazz", "#FE4EDA"});
        g_colorCodes.add(new String[]{"Radical Red", "#FF496C"});
        g_colorCodes.add(new String[]{"Raw Sienna", "#D68A59"});
        g_colorCodes.add(new String[]{"Raw Umber", "#714B23"});
        g_colorCodes.add(new String[]{"Razzle Dazzle Rose", "#FF48D0"});
        g_colorCodes.add(new String[]{"Razzmatazz", "#E3256B"});
        g_colorCodes.add(new String[]{"Red", "#EE204D"});
        g_colorCodes.add(new String[]{"Red Orange", "#FF5349"});
        g_colorCodes.add(new String[]{"Red Violet", "#C0448F"});
        g_colorCodes.add(new String[]{"Robin's Egg Blue", "#1FCECB"});
        g_colorCodes.add(new String[]{"Royal Purple", "#7851A9"});
        g_colorCodes.add(new String[]{"Salmon", "#FF9BAA"});
        g_colorCodes.add(new String[]{"Scarlet", "#FC2847"});
        g_colorCodes.add(new String[]{"Screamin' Green", "#76FF7A"});
        g_colorCodes.add(new String[]{"Sea Green", "#9FE2BF"});
        g_colorCodes.add(new String[]{"Sepia", "#A5694F"});
        g_colorCodes.add(new String[]{"Shadow", "#8A795D"});
        g_colorCodes.add(new String[]{"Shamrock", "#45CEA2"});
        g_colorCodes.add(new String[]{"Shocking Pink", "#FB7EFD"});
        g_colorCodes.add(new String[]{"Silver", "#CDC5C2"});
        g_colorCodes.add(new String[]{"Sky Blue", "#80DAEB"});
        g_colorCodes.add(new String[]{"Spring Green", "#ECEABE"});
        g_colorCodes.add(new String[]{"Sunglow", "#FFCF48"});
        g_colorCodes.add(new String[]{"Sunset Orange", "#FD5E53"});
        g_colorCodes.add(new String[]{"Tan", "#FAA76C"});
        g_colorCodes.add(new String[]{"Teal Blue", "#18A7B5"});
        g_colorCodes.add(new String[]{"Thistle", "#EBC7DF"});
        g_colorCodes.add(new String[]{"Tickle Me Pink", "#FC89AC"});
        g_colorCodes.add(new String[]{"Timberwolf", "#DBD7D2"});
        g_colorCodes.add(new String[]{"Tropical Rain Forest", "#17806D"});
        g_colorCodes.add(new String[]{"Tumbleweed", "#DEAA88"});
        g_colorCodes.add(new String[]{"Turquoise Blue", "#77DDE7"});
        g_colorCodes.add(new String[]{"Unmellow Yellow", "#FFFF66"});
        g_colorCodes.add(new String[]{"Violet (Purple)", "#926EAE"});
        g_colorCodes.add(new String[]{"Violet Blue", "#324AB2"});
        g_colorCodes.add(new String[]{"Violet Red", "#F75394"});
        g_colorCodes.add(new String[]{"Vivid Tangerine", "#FFA089"});
        g_colorCodes.add(new String[]{"Vivid Violet", "#8F509D"});
        g_colorCodes.add(new String[]{"White", "#FFFFFF"});
        g_colorCodes.add(new String[]{"Wild Blue Yonder", "#A2ADD0"});
        g_colorCodes.add(new String[]{"Wild Strawberry", "#FF43A4"});
        g_colorCodes.add(new String[]{"Wild Watermelon", "#FC6C85"});
        g_colorCodes.add(new String[]{"Wisteria", "#CDA4DE"});
        g_colorCodes.add(new String[]{"Yellow", "#FCE883"});
        g_colorCodes.add(new String[]{"Yellow Green", "#C5E384"});
        g_colorCodes.add(new String[]{"Yellow Orange", "#FFAE42"});

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
            for (String[] colorCode : g_colorCodes) {
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
