package org.tavioribeiro.griot.core.domain.shelf.util

object TextNormalizer {

    private val accentMap: Map<Char, Char> = buildMap {
        put('á', 'a'); put('à', 'a'); put('â', 'a'); put('ã', 'a'); put('ä', 'a'); put('å', 'a')
        put('é', 'e'); put('è', 'e'); put('ê', 'e'); put('ë', 'e')
        put('í', 'i'); put('ì', 'i'); put('î', 'i'); put('ï', 'i')
        put('ó', 'o'); put('ò', 'o'); put('ô', 'o'); put('õ', 'o'); put('ö', 'o'); put('ø', 'o')
        put('ú', 'u'); put('ù', 'u'); put('û', 'u'); put('ü', 'u')
        put('ç', 'c'); put('ñ', 'n'); put('ý', 'y'); put('ÿ', 'y')
        put('Á', 'A'); put('À', 'A'); put('Â', 'A'); put('Ã', 'A'); put('Ä', 'A'); put('Å', 'A')
        put('É', 'E'); put('È', 'E'); put('Ê', 'E'); put('Ë', 'E')
        put('Í', 'I'); put('Ì', 'I'); put('Î', 'I'); put('Ï', 'I')
        put('Ó', 'O'); put('Ò', 'O'); put('Ô', 'O'); put('Õ', 'O'); put('Ö', 'O'); put('Ø', 'O')
        put('Ú', 'U'); put('Ù', 'U'); put('Û', 'U'); put('Ü', 'U')
        put('Ç', 'C'); put('Ñ', 'N'); put('Ý', 'Y'); put('Ÿ', 'Y')
    }

    fun normalize(input: String): String {
        if (input.isEmpty()) return input
        return buildString(input.length) {
            for (char in input) {
                val normalized = accentMap[char] ?: char
                append(normalized.lowercaseChar())
            }
        }
    }
}