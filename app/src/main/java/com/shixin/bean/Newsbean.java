package com.shixin.bean;

import java.util.List;

/**
 * Created by admin on 2017/3/3 0003.
 */

public class Newsbean {
        public String status;
        public String message;
        public List<DataBean> data;
        public static class DataBean {
                public String id;
                public String uid;
                public String title;
                public String category;
                public String status;
                public String reason;
                public String sort;
                public String position;
                public String cover;
                public String view;
                public String comment;
                public String collection;
                public String dead_line;
                public String source;
                public String create_time;
                public String update_time;
                public String video;
                public String type;
                public String videopic;
                public String descriptions;
                public int imgsum;
                public List<String> tag;
                public List<String> piclist;
                public List<?> imglist;
        }
}
