package com.rh.oa.gw.util;

/**
 * 正文文件类型
 * @author yangjy
 *
 */
public enum ZW_TYPE implements ZwType {
    /** 正文 **/
    ZHENG_WEN {
        /**
         * @return 编码
         **/
        public String getCode() {
            return "ZHENGWEN";
        }
        
        /**
         * @return 显示名
         */
        public String getName() {
            return "正文";
        }
        
        /**
         * @return 排序号
         */
        public int getSort() {
            return 0;
        }
    },
    /** 加密正文 **/
    ENC_ZHENGWEN {
        /**
         * @return 编码
         **/
        public String getCode() {
            return "ENCZHENGWEN";
        }
        
        /**
         * @return 显示名
         */
        public String getName() {
            return "盖章加密正文";
        }
        
        /**
         * @return 排序号
         */
        public int getSort() {
            return 10;
        }
    },
    /** 红头 **/
    RED_HEAD {
        /**
         * @return 编码
         **/
        public String getCode() {
            return "REDHEAD";
        }
        /**
         * @return 显示名
         */
        public String getName() {
            return "红头文件";
        }
        
        /**
         * @return 排序号
         */
        public int getSort() {
            return 20;
        }
    },
    /** 文稿  **/
    WEN_GAO {
        /**
         * @return 编码
         **/
        public String getCode() {
            return "WENGAO";
        }
        /**
        * @return 显示名
        */
        public String getName() {
            return "文稿";
        }
        
        /**
         * @return 排序号
         */
        public int getSort() {
            return 30;
        }
    }
}
