package com.smilyhomeapplication.css.activities.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.smilyhomeapplication.css.R;
import com.smilyhomeapplication.css.activities.interfaces.IImageSliderClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SlidingImageAdapter extends PagerAdapter {

    private List<String> mArrayList;
    private LayoutInflater inflater;
    private IImageSliderClickListener mSliderClickListener;

    public void setArrayList(List<String> arrayList) {
        mArrayList = arrayList;
    }

    public SlidingImageAdapter(Context context, List<String> images, IImageSliderClickListener sliderClickListener) {
        this.mArrayList = images;
        inflater = LayoutInflater.from(context);
        this.mSliderClickListener = sliderClickListener;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.sliding_images, view, false);
        final ImageView imageView = imageLayout.findViewById(R.id.image);
        Picasso.get().load(mArrayList.get(position)).placeholder(R.drawable.default_image).into(imageView);
        view.addView(imageLayout, 0);
        if (mSliderClickListener != null) {
            imageView.setOnClickListener(view1 -> mSliderClickListener.onGalleryImageClicked());
        }
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
