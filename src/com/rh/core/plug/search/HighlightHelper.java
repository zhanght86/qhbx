package com.rh.core.plug.search;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author yangjy
 * 
 */
public class HighlightHelper {
    private String text = null;
    private int maxLen = 100;
    private List<String> keywords = null;
    private boolean[] isEnglish = null;
    private int[] lens = null;
    private int firstPos = -10;

    /**
     * 
     * @param text 原始字符串
     * @param wordList 关键字
     * @param maxLen 最大长度
     */
    public HighlightHelper(String text, List<String> wordList, int maxLen) {
        this.text = text;
        this.maxLen = maxLen;
        this.keywords = wordList;
        isEnglish = new boolean[wordList.size()];
        Pattern pattern = Pattern.compile("^\\w+$");
        for (int i = 0; i < wordList.size(); i++) {
            Matcher matcher = pattern.matcher(wordList.get(i));
            if (matcher.matches()) {
                isEnglish[i] = true;
            } else {
                isEnglish[i] = false;
            }
        }

        lens = new int[wordList.size()];
        for (int i = 0; i < wordList.size(); i++) {
            lens[i] = wordList.get(i).length();
        }
    }

    /**
     * 
     * @return 取得关键字出现的第一个位置
     */
    public int getFirstPos() {
        if (firstPos < -1) { // 未初始化
            int len = text.length();
            for (int i = 0; i < len; i++) {
                if (match(i)) {
                    firstPos = i;
                    break;
                }
            }

            if (firstPos < -1) {
                firstPos = -1;
            }
        }
        return firstPos;
    }

    /**
     * 
     * @param offset 游标位置
     * @return 是否找到配置的关键字
     */
    private boolean match(int offset) {
        for (int i = 0; i < lens.length; i++) {
            int len = offset + lens[i];
            if (len > text.length()) {
                continue;
            }
            String temp = text.substring(offset, offset + lens[i]);
            if (this.isEnglish[i]) {
                temp = temp.toLowerCase();
            }
            if (temp.equals(this.keywords.get(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取高亮文本
     * @return 返回高亮文本
     */
    public String getHightligtText() {
        int firstPos = getFirstPos();
        if (firstPos < 0) {
            return "";
        }

        String shortStr = getShortText(firstPos, this.maxLen);

        shortStr = shortStr.toLowerCase();

        for (String word : this.keywords) {
            shortStr = shortStr.replace(word, SearchServ.HL_PRE_TAG + word + SearchServ.HL_POST_TAG);
        }
        return shortStr;
    }

    /**
     * 
     * @param index 开始位置
     * @param maxLeng 长度
     * @return 取得缩短的文本
     */
    private String getShortText(int index, int maxLeng) {
        int halfMaxLeng = maxLeng / 2;
        int start = index - halfMaxLeng;
        int end = index + halfMaxLeng;

        // foramt
        if (start < 0) {
            start = 0;
        }
        if (end > text.length()) {
            end = text.length();
        }

        // keep abstract text leng about 100
        if (end - start < maxLeng) {
            int dvalue = maxLeng - (end - start);
            if (start == 0) {
                end += dvalue;
            }
            if (end == text.length()) {
                start -= dvalue;
            }
        }
        // foramt
        if (start < 0) {
            start = 0;
        }

        // if the text not be end of content, we append "..."
        String suffix = "...";
        if (end > text.length()) {
            end = text.length();
        }

        if (end == text.length()) {
            suffix = "";
        }
        String result = text.substring(start, end) + suffix;

        return result;
    }

}
