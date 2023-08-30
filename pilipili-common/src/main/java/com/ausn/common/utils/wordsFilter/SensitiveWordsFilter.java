package com.ausn.common.utils.wordsFilter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * @Author: 付显贵
 * @DateTime: 2023/8/28 0:24
 * @Description:
 */
public class SensitiveWordsFilter
{
    private HashMap<Character,CharacterNode> wordRoots;

    public void buildWordsTree(String fileName) throws IOException
    {
        ClassPathResource classPathResource = new ClassPathResource(fileName);
        InputStream input = classPathResource.getInputStream();



        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        while(reader.ready())
        {
            addNewWord(reader.readLine());
        }

    }

    public void addNewWord(String word)
    {
        CharacterNode pre;
        for(int i=0;i<word.length();i++)
        {
            Character glyph=word.charAt(i);
            CharacterNode node = new CharacterNode(glyph);
            if(i==0)
            {
                wordRoots.put(glyph,node);
                pre=node;
                continue;
            }
            if(i==word.length()-1)
            {
                node.wordEnd=true;
            }
        }
    }



    @Getter
    @Setter
    private class CharacterNode
    {
        private Character glyph;
        private Boolean wordEnd;
        private HashMap<Character,CharacterNode> nextGlyphs;

        CharacterNode(Character glyph)
        {
            nextGlyphs=new HashMap<>();
            wordEnd=false;
            this.glyph=glyph;
        }

        CharacterNode(Character glyph,Boolean isEnd)
        {
            nextGlyphs=new HashMap<>();
            wordEnd=isEnd;
            this.glyph=glyph;
        }
    }
}
