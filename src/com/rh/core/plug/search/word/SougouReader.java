/*
 * Copyright (c) 2011 Ruaho All rights reserved.
 */
package com.rh.core.plug.search.word;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.util.Constant;
import com.rh.core.util.Lang;

/**
 * 从搜狗词库装载入所需词库
 * 
 * @author Jerry Li
 * @version $Id$
 */
public class SougouReader {
    /** log */
	private static Log log = LogFactory.getLog(SougouReader.class);
    /** 字符集 */
	private static String encoding = "UTF-16LE";
    /** 输出流 */
	private ByteArrayOutputStream output = new ByteArrayOutputStream();
	
	/**
	 * 读入搜狗词库文件
	 * @param file 文件
	 * @return 词库解析模型
	 */
    public Bean read(File file) {
        try {
            return read(new FileInputStream(file));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 读入搜狗词库文件
     * @param url 文件URL地址
     * @return 词库解析模型
     */
    public Bean read(URL url) {
        try {
            return read(url.openStream());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 读取输入流指定位置的数据，输出字符串
     * @param input 输入流
     * @param pos 起始位置
     * @param reads 缓存
     * @return 字符串
     * @throws IOException 例外
     */
    protected String readString(DataInputStream input, int pos, int[] reads) throws IOException {
        int read = reads[0];
        input.skip(pos - read);
        read = pos;
        output.reset();
        while (true) {
            int c1 = input.read();
            int c2 = input.read();
            read += 2;
            if (c1 == 0 && c2 == 0) {
                break;
            } else {
                output.write(c1);
                output.write(c2);
            }
        }
        reads[0] = read;
        return new String(output.toByteArray(), encoding);
    }

    /**
     * 将数据流生成搜狗解析模型
     * @param in 输入流
     * @return 解析模型
     */
    public Bean read(InputStream in) {
        Bean wordSet = new Bean();
        DataInputStream input = new DataInputStream(in);
        int read;
        try {
            byte[] bytes = new byte[4];
            input.readFully(bytes);
            assert (bytes[0] == 0x40 && bytes[1] == 0x15 && bytes[2] == 0 && bytes[3] == 0);
            input.readFully(bytes);
            int flag1 = bytes[0];
            assert (bytes[1] == 0x43 && bytes[2] == 0x53 && bytes[3] == 0x01);
            int[] reads = new int[]{8};
            wordSet.set("NAME", readString(input, 0x130, reads));
            wordSet.set("TYPE", readString(input, 0x338, reads));
            wordSet.set("DESCRIPTION", readString(input, 0x540, reads));
            wordSet.set("SAMPLE", readString(input, 0xd40,  reads));
            read = reads[0];
            input.skip(0x1540 - read);
            read = 0x1540;
            input.readFully(bytes);
            read += 4;
            assert (bytes[0] == (byte) 0x9D && bytes[1] == 0x01 && bytes[2] == 0 && bytes[3] == 0);
            bytes = new byte[128];
            Map<Integer, String> pyMap = new LinkedHashMap<Integer, String>();
            while (true) {
                int mark = readUnsignedShort(input);
                int size = input.readUnsignedByte();
                input.skip(1);
                read += 4;
                assert (size > 0 && (size % 2) == 0);
                input.readFully(bytes, 0, size);
                read += size;
                String py = new String(bytes, 0, size, encoding);
                //System.out.println(py);
                pyMap.put(mark, py);
                if ("zuo".equals(py)) {
                    break;
                }
            }
            if (flag1 == 0x44) {
                input.skip(0x2628 - read);
            } else if (flag1 == 0x45) {
                input.skip(0x26C4 - read);
            } else {
                throw new RuntimeException("error file format");
            }
            StringBuffer buffer = new StringBuffer();
            List<Bean> wordList = new ArrayList<Bean>();
            while (true) {
                int size = readUnsignedShort(input);
                if (size < 0) {
                    break;
                }
                int count = readUnsignedShort(input);
                int len = count / 2;
                assert (len * 2 == count);
                buffer.setLength(0);
                for (int i = 0; i < len; i++) {
                    int key = readUnsignedShort(input);
                    buffer.append(pyMap.get(key)).append("'");
                }
                buffer.setLength(buffer.length() - 1);
                for (int i = 0; i < size; i++) {
                    count = readUnsignedShort(input);
                    if (count < 0) {
                        continue;
                    }
                    if (count > bytes.length) {
                        bytes = new byte[count];
                    }
                    input.readFully(bytes, 0, count);
                    String word = new String(bytes, 0, count, encoding);
                    Bean wordBean = new Bean();
                    wordBean.set("WORD_ID", Lang.getUUID());
                    if (word.length() > 40) {
                        log.debug(word);
                        word = word.substring(0, 40);
                    }
                    wordBean.set("WORD_CODE", word);
                    wordBean.set("WORD_LENGTH", word.length());
                    wordBean.set("WORD_FLAG", Constant.NO_INT);
                    //接下来12个字节可能是词频或者类似信息
                    input.skip(12);
                    wordList.add(wordBean);
                }
            }
            log.debug(wordList.size());
            wordSet.set("WORD", wordList);
            return wordSet;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    /**
     * 
     * @param in 输入流
     * @return 结果
     * @throws IOException 例外
     */
    private int readUnsignedShort(InputStream in) throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        if ((ch1 | ch2) < 0) {
            return Integer.MIN_VALUE;
        }
        return (ch2 << 8) + (ch1 << 0);
    }

}