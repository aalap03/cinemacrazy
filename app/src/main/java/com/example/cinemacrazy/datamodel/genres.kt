package com.example.cinemacrazy.datamodel


data class MovieGenres(
    var genres: List<MoviesGenre>
)

data class MoviesGenre(
    var id: Long,
    var name: String
)

var moviesGenre: String = "{\n" +
        "  genres: [\n" +
        "    {\n" +
        "      id: 28,\n" +
        "      name: \"Action\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 12,\n" +
        "      name: \"Adventure\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 16,\n" +
        "      name: \"Animation\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 35,\n" +
        "      name: \"Comedy\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 80,\n" +
        "      name: \"Crime\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 99,\n" +
        "      name: \"Documentary\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 18,\n" +
        "      name: \"Drama\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 10751,\n" +
        "      name: \"Family\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 14,\n" +
        "      name: \"Fantasy\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 36,\n" +
        "      name: \"History\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 27,\n" +
        "      name: \"Horror\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 10402,\n" +
        "      name: \"Music\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 9648,\n" +
        "      name: \"Mystery\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 10749,\n" +
        "      name: \"Romance\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 878,\n" +
        "      name: \"Science Fiction\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 10770,\n" +
        "      name: \"TV Movie\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 53,\n" +
        "      name: \"Thriller\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 10752,\n" +
        "      name: \"War\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 37,\n" +
        "      name: \"Western\"\n" +
        "    }\n" +
        "  ]\n" +
        "}"


data class TvGenres(
    var genres: List<TvGenre>
)

data class TvGenre(
    var id: Long,
    var name: String
)

var tvGenreString: String = "{\n" +
        "  genres: [\n" +
        "    {\n" +
        "      id: 10759,\n" +
        "      name: \"Action & Adventure\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 16,\n" +
        "      name: \"Animation\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 35,\n" +
        "      name: \"Comedy\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 80,\n" +
        "      name: \"Crime\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 99,\n" +
        "      name: \"Documentary\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 18,\n" +
        "      name: \"Drama\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 10751,\n" +
        "      name: \"Family\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 10762,\n" +
        "      name: \"Kids\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 9648,\n" +
        "      name: \"Mystery\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 10763,\n" +
        "      name: \"News\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 10764,\n" +
        "      name: \"Reality\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 10765,\n" +
        "      name: \"Sci-Fi & Fantasy\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 10766,\n" +
        "      name: \"Soap\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 10767,\n" +
        "      name: \"Talk\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 10768,\n" +
        "      name: \"War & Politics\"\n" +
        "    },\n" +
        "    {\n" +
        "      id: 37,\n" +
        "      name: \"Western\"\n" +
        "    }\n" +
        "  ]\n" +
        "}"