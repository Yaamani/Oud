package com.example.tryingstuff;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DesktopTryingStuff {
    public static void main(String[] args) {
        System.out.println("HI");

        //System.out.println(OudApiJsonGenerator.getJsonRecentlyPlayed(6));

        System.out.println(Integer.valueOf("Hi10".substring(3, 4)));

        for (int i = 0; i < 50; i++) {
            //System.out.println(/*"\"playlist" + i + "\": " + */OudApiJsonGenerator.getJsonPlaylist(i, -1) + ", ");
            //System.out.println(OudApiJsonGenerator.getJsonPlaylist(i, -1));
            //System.out.println("\"/playlists/playlist" + i + "\": " + "\"/playlist" + i + "\", ");
            //System.out.println("\"track" + i + "\": " + OudApiJsonGenerator.getJsonTrack(i) + ", ");
            //System.out.println(OudApiJsonGenerator.getJsonAlbum(i) + ", ");
            /*System.out.println("\"category_list_category" + i + "\": " + OudApiJsonGenerator.getJsonCategoryPlaylists(-1) + ", ");*/
        }

        //System.out.println(OudApiJsonGenerator.getJsonArtistAlbumList(7, 7));

        //System.out.println();

        //System.out.println(OudApiJsonGenerator.getJsonRecentlyPlayed(10));


        /*for (int i = 0; i < 50; i++) {
            System.out.println("case \"/playlists/playlist" + i + "\":\n" +
                    "                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(" + i + ", 5));");
        }*/

        //System.out.println(OudApiJsonGenerator.getJsonListOfCategories(7, 7));

        //System.out.println(OudApiJsonGenerator.getJsonLikedTracksList(20, 0));
        //System.out.println(OudApiJsonGenerator.getJsonListOfPlaylists(20, 20));
        //System.out.println(OudApiJsonGenerator.getJsonListOfArtistPreview(20, 20));
        System.out.println(OudApiJsonGenerator.getJsonSavedAlbumsList(20, 0));
        System.out.println(OudApiJsonGenerator.getJsonSavedAlbumsList(20, 20));

        /*Date date = new Date(100000);
        System.out.println(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(date).replace(' ', 'T') + 'Z');*/
    }
}
