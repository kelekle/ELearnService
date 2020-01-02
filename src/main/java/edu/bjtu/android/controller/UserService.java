package edu.bjtu.android.controller;

import com.alibaba.fastjson.JSONObject;
import edu.bjtu.android.dao.UserDao;
import edu.bjtu.android.entity.User;
import edu.bjtu.android.service.MailServiceImpl;
import edu.bjtu.android.util.RedisUtils;
import edu.bjtu.android.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.xml.ws.Response;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/elearn/user")
public class UserService {

    @Value("${users.location}")
    private String filePath;

    @Autowired
    UserDao userDao;

    @Autowired
    private MailServiceImpl mailService;

    @Resource
    private RedisUtils redisUtils;

    @RequestMapping(method = RequestMethod.POST, path = "/login")
    public String login(String email, String password) {
        JSONObject result = new JSONObject();
        User user = userDao.selectByEmail(email);
        if(user == null){
            result.put("result", "fail");
            result.put("msg", "该邮箱未注册!");
            return result.toJSONString();
        }
        if(user.getPassword().equals(password)){
            result.put("result", "success");
            String u = JSONObject.toJSONString(user);
            System.out.println(u);
            result.put("user", JSONObject.toJSON(user));
            result.put("msg", "登陆成功!");
            System.out.println(result.toJSONString());
            return result.toJSONString();
        }
        result.put("result", "fail");
        result.put("msg", "密码错误!");
        System.out.println(result.get("result"));
        System.out.println(result.toJSONString());
        return result.toJSONString();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/register")
    public String register(String email, String password, String code) {
        System.out.println("register");
        Map<String, String> map = new HashMap<>();
        if(getRegistered(email)){
            map.put("result", "该邮箱已注册!");
            return JSONObject.toJSONString(map);
        }
        if(!redisUtils.has(email)) {
            map.put("result", "未发送验证码！");
            return JSONObject.toJSONString(map);
        }
        if(!redisUtils.get(email).equals(code)){
            map.put("result", "验证码错误！");
            return JSONObject.toJSONString(map);
        }
        int uid = userDao.insertByEmail(email, password);
        if(uid <= 0){
            map.put("result", "数据库异常，插入失败，请重新注册!");
        }else {
            File file = new File((filePath + "/" + uid));
            if(!file.exists()){
                file.mkdirs();
            }
            map.put("result", "success");
        }
        return JSONObject.toJSONString(map);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/find_pwd")
    public String findPassword(String email, String password, String code) {
        System.out.println("find_pwd");
        Map<String, String> map = new HashMap<>();
        if(!getRegistered(email)){
            map.put("result", "该邮箱未注册!");
            return JSONObject.toJSONString(map);
        }
        if(!redisUtils.has(email)) {
            map.put("result", "未发送验证码！");
            return JSONObject.toJSONString(map);
        }
        if(!redisUtils.get(email).equals(code)){
            map.put("result", "验证码错误！");
            return JSONObject.toJSONString(map);
        }
        int uid = userDao.changePasswordByEmail(email, password);
        if(uid <= 0){
            map.put("result", "数据库异常，更改密码失败，请重试!");
        }else {
            File file = new File((filePath + "/" + uid));
            if(!file.exists()){
                file.mkdir();
            }
            map.put("result", "success");
        }
        return JSONObject.toJSONString(map);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/get_code")
    public String getCode(String email) {
        return getEmailCode(email);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/test")
    public String test() {
        return "ok";
    }


    @RequestMapping(method = RequestMethod.GET, path = "/get_user")
    public User getUser(String email) {
        return userDao.selectByEmail(email);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/get_user_photo")
    public ResponseEntity<UrlResource> getPhoto(String name, @RequestHeader HttpHeaders headers) throws Exception {
        String path = new File("").getAbsolutePath();
        String vpath = "file:///" + path +"\\"+ filePath + "\\" + name;
        System.out.println(vpath);
        UrlResource doc = new UrlResource(vpath);
        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(doc).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(doc);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/change_username")
    public String changeUsername(String email, String username) {
        Map<String, String> map = new HashMap<>();
        if(userDao.changeUsernameByEmail(email, username) > 0){
            map.put("result", "success");
        }else {
            map.put("result", "fail");
        }
        return JSONObject.toJSONString(map);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/change_description")
    public String changeDescription(String email, String description) {
        Map<String, String> map = new HashMap<>();
        if(userDao.changeDescriptionByEmail(email, description) > 0){
            map.put("result", "success");
        }else {
            map.put("result", "fail");
        }
        return JSONObject.toJSONString(map);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/change_photo")
    public String changePhoto(@RequestPart("email") String email, MultipartFile photo) {
        System.out.println("photo_change");
        System.out.println(email);
        System.out.println(photo.getOriginalFilename());
        User user= userDao.selectByEmail(email);
        Map<String, String> map = new HashMap<>();
        if (!photo.isEmpty()) {
            File file = new File(filePath + "/" + user.getUserid());
            if(file.exists()){
                File[] files = file.listFiles();
                for(int i = 0;i < files.length;i++){
                    files[i].delete();
                }
            }else {
                file.mkdirs();
            }
            try {
                /*
                 * 这段代码执行完毕之后，图片上传到了工程的跟路径； 大家自己扩散下思维，如果我们想把图片上传到
                 * d:/files大家是否能实现呢？ 等等;
                 * 这里只是简单一个例子,请自行参考，融入到实际中可能需要大家自己做一些思考，比如： 1、文件路径； 2、文件名；
                 * 3、文件格式; 4、文件大小的限制;
                 */
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(new File(filePath + "/" + user.getUserid() + "/" +
                                photo.getOriginalFilename())));
                out.write(photo.getBytes());
                out.flush();
                out.close();
                if(userDao.changePhotoByEmail(email, user.getUserid() + "\\" + photo.getOriginalFilename()) > 0){
                    map.put("result", "success");
                }else {
                    map.put("result", "fail");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            map.put("result", "file is empty");
        }
        return JSONObject.toJSONString(map);
    }

    public String getEmailCode(String email){
        Map<String, String> map = new HashMap<>();
        String code = Utils.generateEmailCode();
        mailService.sendSimpleMail(email, "验证码", code);
        System.out.println(email);
        redisUtils.set(email, code);
        System.out.println(code);
        map.put("result", "验证码已发送");
        return JSONObject.toJSONString(map);
    }

    public Boolean getRegistered(String email){
        return userDao.selectByEmail(email) != null;
    }

}
