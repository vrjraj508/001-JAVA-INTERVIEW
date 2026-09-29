package com.basics.string01advanced;

import java.util.*;

class Program001WordLadder{

    public static int ladderLength(
            String beginWord,
            String endWord,
            List<String> wordList
    ){
        Set<String> dictionary = new HashSet<>(wordList);

        if(!dictionary.contains(endWord)){
            return 0;
        }
        Queue<String> queue = new LinkedList<>();

        queue.offer(beginWord);

        Set<String> visited = new HashSet<>();

        visited.add(beginWord);

        int level = 1;

        while(!queue.isEmpty()){
            int size = queue.size();

            for(int i=0;i<size;i++){
                String current = queue.poll();

                if(current.equals(endWord)){
                    return level;
                }

            }
        }
    }

}

public class String01Advanced {

    static void main() {

    }
}
