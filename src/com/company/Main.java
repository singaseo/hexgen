package com.company;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
	// write your code here

        List<Byte> bytes = new ArrayList<>();
        int i =0;
        byte j =0;
        while(i<0xFFFFF){
            i++;
            if(j==127)
                j=0;
            bytes.add(j++);
        }
        HexGen hexGen = new HexGen();
        List<String> res = hexGen.generate(bytes,0xFFF0L,32);

    }
}
