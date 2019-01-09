package niannian.valet.View.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import niannian.valet.R;
import niannian.valet.ResponseModel.SuitResponse;
import niannian.valet.ResponseModel.UrlPic;
import niannian.valet.View.Activity.WearSuitActivity;
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

//    // TODO: Rename and change types of parameters
    public Integer id;
    private String name;

    private ImageView suitImage;

    private OnFragmentInteractionListener mListener;

    public BestSuitFragment() {
        // Required empty public constructor
    }

    public static BestSuitFragment newInstance(Integer id,String name) {
        BestSuitFragment fragment = new BestSuitFragment();
        Bundle args = new Bundle();

        args.putInt("id",id);
        args.putString("name",name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("id");
            name = getArguments().getString("name");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_best_suit, container, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),WearSuitActivity.class);
                intent.putExtra("id",id);//((ImageView)v.findViewById(R.id.bestSuitImage)).getContentDescription().toString());
                intent.putExtra("name",name);//((TextView)v.findViewById(R.id.bestSuitNameText)).getText());
                startActivity(intent);
    }
});

        suitImage = (ImageView)view.findViewById(R.id.bestSuitImage);

        //-1表示该BestSuit实际为空
        if(id!=-1)
            UrlPic.setImage((ImageView)view.findViewById(R.id.bestSuitImage),SuitResponse.url(getActivity(),id));
        else{
            suitImage.setVisibility(View.INVISIBLE);
            TextView noSuitText = view.findViewById(R.id.remindNoFitSuitText);
            noSuitText.setVisibility(View.VISIBLE);
        }

        suitImage.setContentDescription(id.toString());

        TextView suitNameText = view.findViewById(R.id.bestSuitNameText);
        suitNameText.setText(name);

        return view;
    }

    //改到UrlPic中
    private void setImage(ImageView image,String url)
    {
        OkHttpUtils.get().url(url).tag(image)
                .build()
                .connTimeOut(20000).readTimeOut(20000).writeTimeOut(20000)
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(Call call, Bitmap bitmap) {
                        ImageView image = (ImageView)call.request().tag();
                        image.setImageBitmap(bitmap);
                    }
                });
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
