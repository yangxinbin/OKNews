package com.baihui.yangxb.authorMessage;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baihui.yangxb.R;
import com.baihui.yangxb.startapp.User;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class Author extends AppCompatActivity {
    @Bind(R.id.title_back)
    ImageView titleBack;
    @Bind(R.id.title_name)
    TextView titleName;
    @Bind(R.id.title_rightTv)
    TextView titleRightTv;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.imagename)
    TextView imagename;
    @Bind(R.id.imageView)
    CircleImageView imageView;
    @Bind(R.id.imagechange)
    LinearLayout imagechange;
    @Bind(R.id.username)
    TextView username;
    @Bind(R.id.user)
    TextView user;
    //调用照相机返回图片文件
    private File tempFile;
    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static final int CROP_REQUEST_CODE = 3;
    private Bitmap image;
    private User bmobUser;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authormanager);
        ButterKnife.bind(this);
        getImageAndNameFromBmob();
    }
    private void getImageAndNameFromBmob() {
        bmobUser = BmobUser.getCurrentUser(User.class);
        if (bmobUser != null) {
            userName = bmobUser.getUsername();
            Log.v("yxb","------userName----Author---"+userName);
            user.setText(userName);//获得当前用户名
        }
        if (bmobUser != null && userName != null && bmobUser.getAvatar() !=null){
            Glide.with(Author.this)
                    .load(bmobUser.getAvatar().getFileUrl())
                    .placeholder(R.drawable.beginimg)//图片加载出来前，显示的图片
                    .error(R.drawable.imgerror)//图片加载失败后，显示的图片
                    .into(imageView);
        }else {
            //默认图片
            imageView.setImageResource(R.drawable.picture);
        }
    }
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    titleRightTv.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(image);
                    break;
                case 1:
                    //默认图片
                    //Resources res=getResources();
                    //Bitmap bmp= BitmapFactory.decodeResource(res, R.drawable.authormanage);//drawable转为Bitmap
                    imageView.setImageResource(R.drawable.picture);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    /**
     * 从相机获取图片
     */
    private void getPicFromCamera() {
        //用于保存调用相机拍照后所生成的文件
        tempFile = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis() + ".jpg");
        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(Author.this, "com.baihui.yangxb", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {    //否则使用Uri.fromFile(file)方法获取Uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    /**
     * 从相册获取图片
     */
    private void getPicFromAlbm() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);//一定要加 否则fragment 里面的无效
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:   //调用相机后返回
                if (resultCode == RESULT_OK) {
                    //用相机返回的照片去调用剪裁也需要对Uri进行处理
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri contentUri = FileProvider.getUriForFile(Author.this, "com.baihui.yangxb", tempFile);
                        cropPhoto(contentUri);
                    } else {
                        cropPhoto(Uri.fromFile(tempFile));
                    }
                }
                break;
            case ALBUM_REQUEST_CODE:    //调用相册后返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    cropPhoto(uri);
                }
                break;
            case CROP_REQUEST_CODE:     //调用剪裁后返回
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    //在这里获得了剪裁后的Bitmap对象，可以用于上传
                    image = bundle.getParcelable("data");
                    //设置到ImageView上
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            if (user != null) {
                                mHandler.sendEmptyMessage(0);
                            } else {
                                mHandler.sendEmptyMessage(1);
                            }
                        }
                    }.start();

                }
                break;
        }
    }

    @OnClick({R.id.title_back, R.id.title_rightTv, R.id.imagechange})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.title_rightTv:
                Intent i = new Intent();
                Bundle b = new Bundle();
                b.putParcelable("backauthorimg", image);
                i.putExtras(b);
                saveImageToBmob();//绑定用户名
                setResult(RESULT_OK,i);
                finish();
                break;
            case R.id.imagechange:
                showTypeDialog();
                break;
        }
    }

    private void saveImageToBmob() {
        // 假设已知头像文件的本地存储路径如下
        Uri uri = BitMap(image);
        Log.v("yxb","======picPath========="+uri);
        String picPath = "storage/emulated/0/Bmobavatar.png";
        // 创建一个BmobFile对象作为用户头像属性
        final BmobFile avatarFile = new BmobFile(new File(picPath));
        avatarFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    // 头像成功上传到Bmob服务器中，这个时候可以从avatarFile对象中getFileUrl得到服务器中头像的存储地址
                    //String imgString = avatarFile.getFileUrl();//--返回的上传文件的完整地址（带域名）
                    // 头像上传成功后，可以进行用户注册操作了。
                    bmobUser.setAvatar(avatarFile);
                    bmobUser.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Log.v("yxb","======getUsername========="+bmobUser.getUsername());
                                showMsg(1);
                            }else{
                                showMsg(0);
                            }
                        }
                    });
                }else {
                    showMsg(0);
                }
            }
            @Override
            public void onProgress(Integer value) {
                // TODO Auto-generated method stub
                // 返回的上传进度（百分比）
            }
        });

    }

    private void showTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_select_photo, null);
        TextView tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
        TextView tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);
        tv_select_gallery.setOnClickListener(new View.OnClickListener() {// 在相册中选取
            @Override
            public void onClick(View v) {
                getPicFromAlbm();
                dialog.dismiss();
            }
        });
        tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
            @Override
            public void onClick(View v) {
                getPicFromCamera();
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    public void showMsg(int e) {
        Snackbar snackbar = null;
        View view =getWindow().getDecorView();
        View snackbarview = null;
        switch (e){
            case 0:
                snackbar = Snackbar.make(view, getResources().getString(R.string.upload_fail), Snackbar.LENGTH_LONG);
                break;
            case 1:
                snackbar = Snackbar.make(view, getResources().getString(R.string.upload_success), Snackbar.LENGTH_LONG);
                break;
            case 101:
                break;
            default:
                break;
        }
        if (snackbar != null){
            snackbarview = snackbar.getView();
        }
        snackbarview.setBackgroundColor(getResources().getColor(R.color.snackbar));
        TextView tvSnackbarText = (TextView) snackbarview.findViewById(android.support.design.R.id.snackbar_text);
        tvSnackbarText.setTextColor(Color.WHITE);
        snackbar.show();
    }

    //存放图片的地址，可以改动
    private Uri BitMap(Bitmap bitmap){
        File tmpDir=new File(Environment.getExternalStorageDirectory()+"/Bmob");    //保存地址及命名
        if (!tmpDir.exists()){
            tmpDir.mkdir(); //生成目录进行保存
        }
        File img=new File(tmpDir.getAbsolutePath()+"avatar.png");
        try {
            FileOutputStream fos=new FileOutputStream(img);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fos);  //参:压缩的格式，图片质量85，输出流
            fos.flush();
            fos.close();
            Log.v("yxb","-----fromFile-----"+Uri.fromFile(img));
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
