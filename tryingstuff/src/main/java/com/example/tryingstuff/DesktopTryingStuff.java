package com.example.tryingstuff;

public class DesktopTryingStuff {
    public static void main(String[] args) {
        System.out.println("HI");

        //System.out.println(OudApiJsonGenerator.getJsonRecentlyPlayed());

        System.out.println(OudApiJsonGenerator.getJsonAlbum(0));
        System.out.println(OudApiJsonGenerator.getJsonAlbum(1));
        System.out.println(OudApiJsonGenerator.getJsonAlbum(2));
        System.out.println(OudApiJsonGenerator.getJsonAlbum(3));
        System.out.println(OudApiJsonGenerator.getJsonAlbum(4));
        System.out.println(OudApiJsonGenerator.getJsonAlbum(5));
    }
}
