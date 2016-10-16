package com.blue.sky.common.utils;

/**
 * Created by Administrator on 2014/11/22.
 */
public class EnumUtil {

    public enum Login
    {
        Email("电子邮件"),
        Mobile("手机"),
        QQ("QQ"),
        Sina("新浪微博");
        // 成员变量
        private String name;

        // 构造方法
        private Login(String name) {
            this.name = name;
        }
    }


    public enum Navigation{

        Assistant("生活助手"),
        File("文件管理"),
        App("应用管理"),
        Image("图片管理"),
        Music("音乐管理"),
        Video("视频管理"),
        Doc("文档管理"),
        Game("轻松一刻");

        public String name;


        private Navigation(String name) {
            this.name = name;
        }
    }
    public static void main(String[] args){

        System.out.println(Navigation.Assistant.name);


    }

}
