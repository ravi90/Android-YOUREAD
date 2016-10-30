package com.eilifint.ravimal.youread;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Ravimal on 9/24/2016.
 * {@link CategoryAdapter} is an {@link ArrayAdapter} that can provide the layout for each category
 * based on a data source, which is a list of {@link Category} objects.
 */

public class CategoryAdapter extends ArrayAdapter<Category> {

    /**
     * Create a new {@link CategoryAdapter} object.
     *
     * @param context is the current context (i.e. Activity) that the adapter is being created in.
     * @param objects is the list of {@link Category}s to be displayed.
     */
    public CategoryAdapter(Context context, ArrayList<Category> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.category_item, parent, false);

        }

        // Get the {@link Category} object located at this position in the list
        Category currentCategory = getItem(position);

        // Create a new {@link Holder} object
        Holder categoryHolder = new Holder();

        // Find the TextView in the category_item.xml layout with the ID title_text_view
        categoryHolder.titleTextView = (TextView) listItemView.findViewById(R.id.title_text_view);
        // Get the title from the current Category object and
        // set this text on the titleTextView TextView
        categoryHolder.titleTextView.setText(currentCategory.getTitle());

        // Find the ImageView in the category_item.xml layout with the ID category_image_view
        categoryHolder.categoryImage = (ImageView) listItemView.findViewById(R.id.category_image_view);
        // Get the getImageResourceId from the current Category object and
        // set this text on the categoryImage ImageView
        categoryHolder.categoryImage.setImageResource(currentCategory.getImageResourceId());

        // Return the whole list item layout so that it can be shown the ListView.
        return listItemView;
    }

    /**
     * {@link NewsAdapter.Holder} is to hold textView and ImageView
     */
    private static class Holder {

        //Declaration
        private TextView titleTextView;
        private ImageView categoryImage;


    }
}
