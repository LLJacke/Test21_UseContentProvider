package com.lljackie.test21_usecontentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

public class Words {
    private static final String AUTHORITY = "com.lljackie.wordsprovider";

    public Words() {
    }

    static abstract class Word implements BaseColumns {
        static final String TABLE_NAME = "words";//表名
        static final String COLUMN_NAME_WORD = "word";//列：单词
        static final String COLUMN_NAME_MEANING = "meaning";//列：单词含义
        static final String COLUMN_NAME_SAMPLE = "sample";//列：单词示例

        //MIME类型
        static final String MIME_DIR_PREFIX = "vnd.android.cursor.dir";
        static final String MIME_ITEM_PREFIX = "vnd.android.cursor.item";
        static final String MINE_ITEM = "vnd.lljackie.word";

        static final String MINE_TYPE_SINGLE = MIME_ITEM_PREFIX + "/" + MINE_ITEM;
        static final String MINE_TYPE_MULTIPLE = MIME_DIR_PREFIX + "/" + MINE_ITEM;

        static final String PATH_SINGLE = "word/#";//单条数据的路径
        static final String PATH_MULTIPLE = "word";//多条数据的路径

        //Content Uri
        static final String CONTENT_URI_STRING = "content://" + AUTHORITY + "/" + PATH_MULTIPLE;
        static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);

    }
}
