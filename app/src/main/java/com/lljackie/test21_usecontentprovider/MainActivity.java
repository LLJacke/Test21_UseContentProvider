package com.lljackie.test21_usecontentprovider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.lljackie.test21_usecontentprovider.Words.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.id;

/**
 * 将原来课件中简化的版本做了些修改：
 * 1.用ListView显示结果
 * 2.可输入id选择修改删除的目标
 *
 * 时间关系，其他可优化的内容不做修改
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Test21_Tag";
    private ContentResolver resolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resolver = this.getContentResolver();
        show();

        Button bt_add = (Button) findViewById(R.id.bt_add);
        Button bt_delete = (Button) findViewById(R.id.bt_delete);
        Button bt_deleteall = (Button) findViewById(R.id.bt_deleteall);
        Button bt_update = (Button) findViewById(R.id.bt_update);
        Button bt_search = (Button) findViewById(R.id.bt_search);

        //去掉了显示按钮，用show()方法直接显示在右侧ListView中，即时更新

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strWord = "Banana";
                String strMeaning = "banana";
                String strSample = "This banana is very nice.";
                ContentValues values = new ContentValues();

                values.put(Words.Word.COLUMN_NAME_WORD, strWord);
                values.put(Words.Word.COLUMN_NAME_MEANING, strMeaning);
                values.put(Words.Word.COLUMN_NAME_SAMPLE, strSample);

                Uri newUri = resolver.insert(Words.Word.CONTENT_URI, values);

                show();
            }
        });

        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final LinearLayout login = (LinearLayout) inflater.inflate(R.layout.insert_id,null);
                final EditText et_id = (EditText) login.findViewById(R.id.et_id);

                builder.setView(login)
                        .setTitle("Input the ID:")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String id = et_id.getText().toString();

                                Uri uri = Uri.parse(Words.Word.CONTENT_URI_STRING + "/" + id);
                                resolver.delete(uri, null, null);

                                show();
                            }
                        });
                builder.show();


            }
        });

        bt_deleteall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolver.delete(Words.Word.CONTENT_URI, null, null);
                show();
            }
        });

        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String strWord = "Apple";
                final String strMeaning = "apple";
                final String strSample = "This apple is very nice.";

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final LinearLayout login = (LinearLayout) inflater.inflate(R.layout.insert_id,null);
                final EditText et_id = (EditText) login.findViewById(R.id.et_id);

                builder.setView(login)
                        .setTitle("Input the ID:")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String id = et_id.getText().toString();

                                ContentValues values = new ContentValues();

                                values.put(Words.Word.COLUMN_NAME_WORD, strWord);
                                values.put(Words.Word.COLUMN_NAME_MEANING, strMeaning);
                                values.put(Words.Word.COLUMN_NAME_SAMPLE, strSample);

                                Uri uri = Uri.parse(Words.Word.CONTENT_URI_STRING + "/" + id);
                                resolver.update(uri, values, null, null);

                                show();
                            }
                        });
                builder.show();


            }
        });

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(Words.Word.CONTENT_URI_STRING + "/" + id);
                final Cursor cursor = resolver.query(Words.Word.CONTENT_URI,
                        new String[]{Words.Word._ID, Words.Word.COLUMN_NAME_WORD,
                                Words.Word.COLUMN_NAME_MEANING, Words.Word.COLUMN_NAME_SAMPLE},
                        null, null, null);
                if (cursor == null) {
                    Toast.makeText(MainActivity.this, "没有找到记录", Toast.LENGTH_LONG).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final LinearLayout login = (LinearLayout) inflater.inflate(R.layout.insert_id,null);
                final EditText et_id = (EditText) login.findViewById(R.id.et_id);

                builder.setView(login)
                        .setTitle("Input the ID:")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String id = et_id.getText().toString();

                                String msg = "";
                                if (cursor.moveToFirst()) {
                                    do {
                                        msg += "ID:" + cursor.getInt(cursor.getColumnIndex(Words.Word._ID)) + ",";
                                        msg += "单词：" + cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_WORD)) + ",";
                                        msg += "含义：" + cursor.getInt(cursor.getColumnIndex(Words.Word.COLUMN_NAME_MEANING)) + ",";
                                        msg += "示例" + cursor.getFloat(cursor.getColumnIndex(Words.Word.COLUMN_NAME_SAMPLE)) + "\n";
                                    } while (cursor.moveToNext());
                                }

                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                            }
                        });
                builder.show();


            }
        });
    }

    private void setWordsListView(ArrayList<Map<String, String>> items) {
        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.item,
                new String[]{Word._ID, Word.COLUMN_NAME_WORD,
                        Word.COLUMN_NAME_MEANING, Word.COLUMN_NAME_SAMPLE},
                new int[]{R.id.textId, R.id.textViewWord, R.id.textViewMeaning, R.id.textViewSample});

        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
    }

    private ArrayList<Map<String, String>> ConvertCursor2List(Cursor cursor) {
        ArrayList<Map<String, String>> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            map.put(Word._ID, String.valueOf(cursor.getInt(0)));
            map.put(Word.COLUMN_NAME_WORD, cursor.getString(1));
            map.put(Word.COLUMN_NAME_MEANING, cursor.getString(2));
            map.put(Word.COLUMN_NAME_SAMPLE, cursor.getString(3));
            result.add(map);
        }
        return result;
    }

    private void show() {
        resolver = this.getContentResolver();
        Cursor cursor = resolver.query(Words.Word.CONTENT_URI,
                new String[]{Words.Word._ID, Words.Word.COLUMN_NAME_WORD,
                        Words.Word.COLUMN_NAME_MEANING, Words.Word.COLUMN_NAME_SAMPLE},
                null, null, null);

        ArrayList<Map<String, String>> items = ConvertCursor2List(cursor);
        setWordsListView(items);
    }
}
