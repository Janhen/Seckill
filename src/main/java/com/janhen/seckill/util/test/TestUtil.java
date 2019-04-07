package com.janhen.seckill.util.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.janhen.seckill.pojo.SeckillUser;
import com.janhen.seckill.util.KeyUtil;
import com.janhen.seckill.util.MD5Util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestUtil {

    private static final String LOGIN_URL = "http://localhost:8082/user/do_login2";

    private static final String FILE_PATH = "D:/tokens.txt";

    private static void createUser(int count) throws Exception {
        List<SeckillUser> users = new ArrayList<>(count);
        //生成用户
        for (int i = 0; i < count; i++) {
            SeckillUser user = new SeckillUser();
            user.setId(KeyUtil.geneUserId());
            user.setLoginCount(1);
            user.setNickname("user" + i);
            user.setRegisterDate(new Date());
            user.setSalt("1a2b3c");
            user.setPassword(MD5Util.inputPassToDBPass("123456", user.getSalt()));
            users.add(user);
        }
        System.out.println("create user");
		//插入数据库
		Connection conn = DBUtil.getConn();
		String sql = "INSERT INTO seckill_user(login_count, nickname, register_date, salt, password, id) VALUES (?,?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		for(int i=0;i<users.size();i++) {
			SeckillUser user = users.get(i);
			pstmt.setInt(1, user.getLoginCount());
			pstmt.setString(2, user.getNickname());
			pstmt.setTimestamp(3, new Timestamp(user.getRegisterDate().getTime()));
			pstmt.setString(4, user.getSalt());
			pstmt.setString(5, user.getPassword());
			pstmt.setLong(6, user.getId());
			pstmt.addBatch();
		}
		pstmt.executeBatch();
		pstmt.close();
		conn.close();
		System.out.println("insert to db");


        //登录，生成token

        File file = new File(FILE_PATH);
        if (file.exists()) {
            file.delete();
        }

        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);
        for (int i = 0; i < users.size(); i++) {
            SeckillUser user = users.get(i);
            URL url = new URL(LOGIN_URL);
            HttpURLConnection co = (HttpURLConnection) url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream out = co.getOutputStream();
            String params = "mobile=" + user.getId() + "&password=" + MD5Util.inputPassToFormPass("123456");
            out.write(params.getBytes());
            out.flush();

            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte buff[] = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0, len);
            }
            inputStream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            JSONObject jo = JSON.parseObject(response);
            String token = jo.getString("data");
            System.out.println("create token : " + user.getId());

            String row = user.getId() + "," + token;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file : " + user.getId());
        }
        raf.close();

        System.out.println("over");
    }

    public static void main(String[] args) throws Exception {
        createUser(10);
    }
}