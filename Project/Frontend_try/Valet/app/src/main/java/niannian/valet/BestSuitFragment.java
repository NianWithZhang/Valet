package niannian.valet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.io.InputStream;

import okhttp3.Call;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BestSuitFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BestSuitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BestSuitFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
    private static final String URL = "https://i2.hdslb.com/bfs/archive/931203046d1dba2b69c472e9a6ff294e47491609.jpg@160w_100h.jpg";

//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
    private String url;

    private OnFragmentInteractionListener mListener;

    public BestSuitFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BestSuitFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BestSuitFragment newInstance() {
        BestSuitFragment fragment = new BestSuitFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        args.putString(URL,_url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//url = getArguments().getString(URL);
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_best_suit, container, false);
//        setImage((ImageView)view.findViewById(R.id.testImage));
        return view;
    }

//    private void setImage(ImageView image)
//    {
//        OkHttpUtils.get().url(url).tag(image)
//                .build()
//                .connTimeOut(20000).readTimeOut(20000).writeTimeOut(20000)
//                .execute(new BitmapCallback() {
//                    @Override
//                    public void onError(Call call, Exception e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Bitmap bitmap) {
//                        ImageView image = (ImageView)call.request().tag();
//                        image.setImageBitmap(bitmap);
//                    }
//                });
//    }


//    private void initView(ImageView image) {
//        // TODO Auto-generated method stub
//        BitmapUtils bitmapUtils = new BitmapUtils(this);
//        // 加载网络图片
//        bitmapUtils.display(image,
//                "https://img-my.csdn.net/uploads/201407/26/1406383290_9329.jpg");
//
//        // 加载本地图片(路径以/开头， 绝对路径)
//        // bitmapUtils.display(imageView, "/sdcard/test.jpg");
//
//        // 加载assets中的图片(路径以assets开头)
//        // bitmapUtils.display(imageView, "assets/img/wallpaper.jpg");
//
//    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
