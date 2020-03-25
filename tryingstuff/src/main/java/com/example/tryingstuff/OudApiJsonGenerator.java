package com.example.tryingstuff;

public class OudApiJsonGenerator {

    public static final String[] PORTRAITS = {
            "https://preview.redd.it/b8y11p45efo41.jpg?width=640&crop=smart&auto=webp&s=c5812cbdd8da57657396e933c411489e337b0d41",
            "https://preview.redd.it/81hbz34ggdo41.jpg?width=640&crop=smart&auto=webp&s=ab2b5493e6027a261a4e01f8a0fc1f807247b22f",
            "https://preview.redd.it/jtk5l23c2jn41.png?width=640&crop=smart&auto=webp&s=e7374b18b4f25011f6fc1d6e47de6d2af0326c45",
            "https://preview.redd.it/cqbbdskakao41.jpg?width=640&crop=smart&auto=webp&s=f097ab88c95b0c6faadafe2981d4d17310166b5f",
            "https://preview.redd.it/m13wwgnv81o41.jpg?width=640&crop=smart&auto=webp&s=3e8389f267e575638145510f50f18c757c756b60",
            "https://preview.redd.it/gy1pczk5cwn41.jpg?width=640&crop=smart&auto=webp&s=20ec76334cfaef6960b116d60b8cbdb5f494c50d",
            "https://preview.redd.it/bsbed8u7xqn41.jpg?width=640&crop=smart&auto=webp&s=3adc502785791875d56b4c0476727f5f92a8ed8e"
    };

    public static final String[] VECTOR_ART = {
            "https://preview.redd.it/tgo1fl9xdgo41.png?width=640&crop=smart&auto=webp&s=6b82d0c5dc1baa6722c89c4d5d3870caef6a77f7",
            "https://preview.redd.it/kl84hi4j53o41.png?width=640&crop=smart&auto=webp&s=3c5026f192a9bfb8028d80c6f22c8ad161c627e3",
            "https://preview.redd.it/3nn2z153s5o41.png?width=640&crop=smart&auto=webp&s=d2027ad0b659b455ec42f37c53f865119b4793df",
            "https://preview.redd.it/jtk5l23c2jn41.png?width=640&crop=smart&auto=webp&s=e7374b18b4f25011f6fc1d6e47de6d2af0326c45",
            "https://preview.redd.it/ivjv5kyv1in41.jpg?width=640&crop=smart&auto=webp&s=aa9386b9408bb57c2ab818c9658ee247b9d974a2",
            "https://preview.redd.it/28ajodxzbfn41.jpg?width=640&crop=smart&auto=webp&s=24aa6caa8e5e833fc651d0da73f338d15a8d66a4",
            "https://preview.redd.it/f5m4o1rc1an41.png?width=640&crop=smart&auto=webp&s=92bfae8d1f55a9c0f377a2f824120bfeaa2879f0"
    };

    public static final int JSON_GENERATION_ALBUM_TRACKS_COUNT = 10;




    public static String getJsonRecentlyPlayed() {
        return "{\n" +
               "    \"items\": [ \n" +
               "        {\"track\": {\"_id\": \"track0\",\"name\": \"track0\",\"artists\": [{\"_id\": \"artist0\",\"name\": \"artist0\",\"type\": \"artist_type0\",\"image\": \"https://preview.redd.it/b8y11p45efo41.jpg?width=640&crop=smart&auto=webp&s=c5812cbdd8da57657396e933c411489e337b0d41\"}],\"albumId\": \"album0\",\"type\": \"track_type0\",\"audioUrl\": \"string\",\"duartion\": 100,\"views\": 1000},\"playedAt\": \"2020-03-21T14:08:03Z\",\"context\": {\"type\": \"context_type0\",\"id\": \"context0\"}} , \n" +
               "        {\"track\": {\"_id\": \"track1\",\"name\": \"track1\",\"artists\": [{\"_id\": \"artist1\",\"name\": \"artist1\",\"type\": \"artist_type1\",\"image\": \"https://preview.redd.it/81hbz34ggdo41.jpg?width=640&crop=smart&auto=webp&s=ab2b5493e6027a261a4e01f8a0fc1f807247b22f\"}],\"albumId\": \"album1\",\"type\": \"track_type1\",\"audioUrl\": \"string\",\"duartion\": 101,\"views\": 1001},\"playedAt\": \"2020-03-21T14:08:03Z\",\"context\": {\"type\": \"context_type1\",\"id\": \"context1\"}} , \n" +
               "        {\"track\": {\"_id\": \"track2\",\"name\": \"track2\",\"artists\": [{\"_id\": \"artist2\",\"name\": \"artist2\",\"type\": \"artist_type2\",\"image\": \"https://preview.redd.it/jtk5l23c2jn41.png?width=640&crop=smart&auto=webp&s=e7374b18b4f25011f6fc1d6e47de6d2af0326c45\"}],\"albumId\": \"album2\",\"type\": \"track_type2\",\"audioUrl\": \"string\",\"duartion\": 102,\"views\": 1002},\"playedAt\": \"2020-03-21T14:08:03Z\",\"context\": {\"type\": \"context_type2\",\"id\": \"context2\"}} , \n" +
               "        {\"track\": {\"_id\": \"track3\",\"name\": \"track3\",\"artists\": [{\"_id\": \"artist3\",\"name\": \"artist3\",\"type\": \"artist_type3\",\"image\": \"https://preview.redd.it/cqbbdskakao41.jpg?width=640&crop=smart&auto=webp&s=f097ab88c95b0c6faadafe2981d4d17310166b5f\"}],\"albumId\": \"album3\",\"type\": \"track_type3\",\"audioUrl\": \"string\",\"duartion\": 103,\"views\": 1003},\"playedAt\": \"2020-03-21T14:08:03Z\",\"context\": {\"type\": \"context_type3\",\"id\": \"context3\"}} , \n" +
               "        {\"track\": {\"_id\": \"track4\",\"name\": \"track4\",\"artists\": [{\"_id\": \"artist4\",\"name\": \"artist4\",\"type\": \"artist_type4\",\"image\": \"https://preview.redd.it/m13wwgnv81o41.jpg?width=640&crop=smart&auto=webp&s=3e8389f267e575638145510f50f18c757c756b60\"}],\"albumId\": \"album4\",\"type\": \"track_type4\",\"audioUrl\": \"string\",\"duartion\": 104,\"views\": 1004},\"playedAt\": \"2020-03-21T14:08:03Z\",\"context\": {\"type\": \"context_type4\",\"id\": \"context4\"}} , \n" +
               "        {\"track\": {\"_id\": \"track5\",\"name\": \"track5\",\"artists\": [{\"_id\": \"artist5\",\"name\": \"artist5\",\"type\": \"artist_type5\",\"image\": \"https://preview.redd.it/gy1pczk5cwn41.jpg?width=640&crop=smart&auto=webp&s=20ec76334cfaef6960b116d60b8cbdb5f494c50d\"}],\"albumId\": \"album5\",\"type\": \"track_type5\",\"audioUrl\": \"string\",\"duartion\": 105,\"views\": 1005},\"playedAt\": \"2020-03-21T14:08:03Z\",\"context\": {\"type\": \"context_type5\",\"id\": \"context5\"}}" +
               "    ],\n" +
               "    \"limit\": 6\n" +
               "}";
    }

    public static String getJsonAlbum(int i) {
        String s = "{" +
                "\"_id\": \"album" + i + "\"," +
                "  \"album_type\": \"album_type" + i + "\"," +
                "  \"artists\": [" +
                getJsonArtistPreview(i) +
                "  ]," +
                "  \"genres\": [" +
                "    \"genre" + i / 2 + "\", " +
                "    \"genre" + i + "\"" +
                "  ]," +
                "  \"image\": \"" + VECTOR_ART[i] + "\"," +
                "  \"name\": \"album" + i + "\"," +
                "  \"release_date\": \"2020-03-21\"," +
                "  \"tracks\": {" +
                "    \"items\": [";
        for (int _i = 0; _i < JSON_GENERATION_ALBUM_TRACKS_COUNT; _i++) {
            s += getJsonTrack(_i + i*JSON_GENERATION_ALBUM_TRACKS_COUNT);
            if (_i < JSON_GENERATION_ALBUM_TRACKS_COUNT - 1)
                s+= ", ";
        }
        s +=
                "    ]," +
                        "    \"limit\": " + JSON_GENERATION_ALBUM_TRACKS_COUNT + "," +
                        "    \"offset\": 0," +
                        "    \"total\": " + JSON_GENERATION_ALBUM_TRACKS_COUNT + "" +
                        "  }," +
                        "  \"type\": \"type" + i + "\"," +
                        "  \"released\": true" +
                        "}";
        return s;

    }

    public static String getJsonTrack(int i) {
        int artistIndex = i/JSON_GENERATION_ALBUM_TRACKS_COUNT;
        int albumIndex = i/JSON_GENERATION_ALBUM_TRACKS_COUNT;
        return "{" +
                "  \"_id\": \"track" + i + "\"," +
                "  \"name\": \"track" + i + "\"," +
                "  \"artists\": [" +
                getJsonArtistPreview(artistIndex) +
                "  ]," +
                "  \"albumId\": \"album" + albumIndex + "\"," +
                "  \"type\": \"string\"," +
                "  \"audioUrl\": \"string\"," +
                "  \"duartion\": 100" + i + "," +
                "  \"views\": 100" + i + "" +
                "}";
    }

    public static String getJsonArtistPreview(int i) {
        return "{" +
                "   \"_id\": \"artist" + i + "\"," +
                "   \"name\": \"artist" + i + "\"," +
                "   \"type\": \"artist_type" + i + "\"," +
                "   \"image\": \"" + PORTRAITS[i] + "\"" +
                "}";
    }
}
