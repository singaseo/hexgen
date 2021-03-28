package com.company;

import java.util.ArrayList;
import java.util.List;

public class HexGen {
    HexGen(){

    }

    public List<String> generate(List<Byte> inputBytes,long startAddress,int lineLength){
        int leftBytes = inputBytes.size();
        List<String> res = new ArrayList<>();

        long baseAddress = startAddress & 0xFFFF0000L;
        long lowerAddress = startAddress & 0xFFFFL;
        int bytesIndex = 0;

        while(leftBytes>0){
            int thisLineLength = (leftBytes>lineLength) ? lineLength : leftBytes;

            if(lowerAddress + thisLineLength > 0xFFFFL){
                thisLineLength = (int)(0x10000L-lowerAddress);
                res.add(generateDataLine(lowerAddress,thisLineLength,inputBytes,bytesIndex));
                bytesIndex += thisLineLength;
                lowerAddress = 0x0000L;
                leftBytes -= thisLineLength;
                thisLineLength = (leftBytes>lineLength) ? lineLength : leftBytes;

                baseAddress += 0x10000L;
                res.add(generateExtendedLinearLine(baseAddress>>16));
            }

            res.add(generateDataLine(lowerAddress,thisLineLength,inputBytes,bytesIndex));
            bytesIndex += thisLineLength;
            lowerAddress += thisLineLength;
            leftBytes -= thisLineLength;
        }
        res.add(generateEndOfFileLine());
        return res;
    }

    private String generateEndOfFileLine(){
        return ":00000001FF";
    }

    private String generateExtendedLinearLine(long address){
        StringBuilder sb = new StringBuilder();
        sb.append(":02000004");
        sb.append(String.format("%04X",address));
        sb.append(checksum(sb.toString().substring(1)));
        return sb.toString();
    }

    private String generateDataLine(long address,int length,List<Byte> buf,int bufIndex){
        StringBuilder sb =  new StringBuilder();
        sb.append(":");
        sb.append(String.format("%02X",length));
        sb.append(String.format("%04X",address&0xFFFFL));
        sb.append("00");
        for(int i=bufIndex;i<bufIndex+length;i++)
            sb.append(String.format("%02X",buf.get(i)));
        sb.append(checksum(sb.toString().substring(1)));
        return sb.toString();
    }

    private String checksum(String line){
        int sum = 0;
        for(int i=0;i<line.length();i+=2)
            sum += Integer.parseInt(line.substring(i,i+2),16);
        sum = ~sum;
        sum += 1;
        return String.format("%02X",sum & 0xFF);
    }

}
