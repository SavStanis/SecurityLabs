package com.savstanis.crypto.substitution;

import java.util.ArrayList;
import java.util.List;

public class PolySubstitutionCipherAttack5 {
    private static final String MESSAGE_5 = "UMUPLYRXOYRCKTYYPDYZTOUYDZHYJYUNTOMYTOLTKAOHOKZCMKAVZDYBRORPTHQLSERUOERMKZGQJOIDJUDNDZATUVOTTLMQBOWNMERQTDTUFKZCMTAZMEOJJJOXMERKJHACMTAZATIZOEPPJKIJJNOCFEPLFBUNQHHPPKYYKQAZKTOTIKZNXPGQZQAZKTOTIZYNIUISZIAELMKSJOYUYYTHNEIEOESULOXLUEYGBEUGJLHAJTGGOEOSMJHNFJALFBOHOKAGPTIHKNMKTOUUUMUQUDATUEIRBKYUQTWKJKZNLDRZBLTJJJIDJYSULJARKHKUKBISBLTOJRATIOITHYULFBITOVHRZIAXFDRNIORLZEYUUJGEBEYLNMYCZDITKUXSJEJCFEUGJJOTQEZNORPNUDPNQIAYPEDYPDYTJAIGJYUZBLTJJYYNTMSEJYFNKHOTJARNLHHRXDUPZIALZEDUYAOSBBITKKYLXKZNQEYKKZTOKHWCOLKURTXSKKAGZEPLSYHTMKRKJIIQZDTNHDYXMEIRMROGJYUMHMDNZIOTQEKURTXSKKAGZEPLSYHTMKRKJIIQZDTNROAUYLOTIMDQJYQXZDPUMYMYPYRQNYFNUYUJJEBEOMDNIYUOHYYYJHAOQDRKKZRRJEPCFNRKJUHSJOIRQYDZBKZURKDNNEOYBTKYPEJCMKOAJORKTKJLFIOQHYPNBTAVZEUOBTKKBOWSBKOSKZUOZIHQSLIJJMSURHYZJJZUKOAYKNIYKKZNHMITBTRKBOPNUYPNTTPOKKZNKKZNLKZCFNYTKKQNUYGQJKZNXYDNJYYMEZRJJJOXMERKJVOSJIOSIQAGTZYNZIOYSMOHQDTHMEDWJKIULNOTBCALFBJNTOGSJKZNEEYYKUIXLEUNLNHNMYUOMWHHOOQNUYGQJKZLZJZLOLATSEHQKTAYPYRZJYDNQDTHBTKYKYFGJRRUFEWNTHAXFAHHODUPZMXUMKXUFEOTIMUNQIHGPAACFKATIKIZBTOTIKZNKKZNLORUKMLLFBUUQKZNLEOHIEOHEDRHXOTLMIRKLEAHUYXCZYTGUYXCZYTIUYXCZYTCVJOEBKOHE";

    public static void main(String[] args) {
        var divided = divideMessage(MESSAGE_5, 26);
        List<String> decipheredParts = new ArrayList<>();

        divided.forEach((partition) -> {
            decipheredParts.add(new GeneticAlgorithm().run(partition));
        });
        System.out.println(rebuildMessage(decipheredParts));
    }

    private static List<String> divideMessage(String message, int keySize) {
        List<String> resultList = new ArrayList<>();

        for (int i = 0; i < keySize; i++) {
            StringBuilder stringBuilder = new StringBuilder();

            for (int j = i; j < message.length(); j += keySize) {
                stringBuilder.append(message.charAt(j));
            }

            resultList.add(stringBuilder.toString());
        }

        return resultList;
    }

    private static String rebuildMessage(List<String> dividedDeciphered) {
        StringBuilder stringBuilder = new StringBuilder();

        var counter = 0;
        boolean formed = false;

        while (!formed) {
            for (String m : dividedDeciphered) {
                if (m.length() <= counter) {
                    formed = true;
                    continue;
                }

                stringBuilder.append(m.charAt(counter));
            }
            counter++;
        }

        return stringBuilder.toString();
    }
}
