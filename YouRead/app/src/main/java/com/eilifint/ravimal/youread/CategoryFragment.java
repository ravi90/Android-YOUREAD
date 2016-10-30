package com.eilifint.ravimal.youread;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * CategoryFragment is  {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    /**
     * Default constructor
     */
    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //binding view to use the category_list.xml layout file
        View rootView = inflater.inflate(R.layout.category_list, container, false);
        //ArrayList to store set of {@link Category} objects
        final ArrayList<Category> categories = new ArrayList<Category>();


        // Create a list of Category
        categories.add(new Category(this.getString(R.string.category_business), R.drawable.image_business_min));
        categories.add(new Category(this.getString(R.string.category_celebrities), R.drawable.image_celebrities_min));
        categories.add(new Category(this.getString(R.string.category_economy), R.drawable.image_economy_min));
        categories.add(new Category(this.getString(R.string.category_education), R.drawable.image_education_min));
        categories.add(new Category(this.getString(R.string.category_entertainment), R.drawable.image_entertainment_min));
        categories.add(new Category(this.getString(R.string.category_fashion), R.drawable.image_fashion_min));
        categories.add(new Category(this.getString(R.string.category_finance), R.drawable.image_finance_min));
        categories.add(new Category(this.getString(R.string.category_fitness), R.drawable.image_fitness_min));
        categories.add(new Category(this.getString(R.string.category_food), R.drawable.image_food_min));
        categories.add(new Category(this.getString(R.string.category_gaming), R.drawable.image_gaming_min));
        categories.add(new Category(this.getString(R.string.category_health), R.drawable.image_health_min));
        categories.add(new Category(this.getString(R.string.category_history), R.drawable.image_history_min));
        categories.add(new Category(this.getString(R.string.category_law), R.drawable.image_law_min));
        categories.add(new Category(this.getString(R.string.category_life_style), R.drawable.image_lifestyle_min));
        categories.add(new Category(this.getString(R.string.category_marketing), R.drawable.image_marketting_min));
        categories.add(new Category(this.getString(R.string.category_medical), R.drawable.image_medical_min));
        categories.add(new Category(this.getString(R.string.category_politics), R.drawable.image_politics_min));
        categories.add(new Category(this.getString(R.string.category_science), R.drawable.images_science));
        categories.add(new Category(this.getString(R.string.category_shopping), R.drawable.image_shoppipng_min));
        categories.add(new Category(this.getString(R.string.category_social_media), R.drawable.image_social_media_min));
        categories.add(new Category(this.getString(R.string.category_sports), R.drawable.image_sports_min));
        categories.add(new Category(this.getString(R.string.category_technology), R.drawable.image_technology_min));
        categories.add(new Category(this.getString(R.string.category_travel), R.drawable.image_travel_min));
        categories.add(new Category(this.getString(R.string.category_weather), R.drawable.image_weather_min));

        /*Create an {@link CategoryAdapter}, whose data source is a list of {@link Category}s. The
         adapter knows how to create list items for each item in the list.*/
        final CategoryAdapter adapter = new CategoryAdapter(getActivity(), categories);

         /*Find the {@link ListView} object in the view hierarchy of the {@link Fragment}.
         There should be a {@link ListView} with the view ID called list, which is declared in the
         category_list.xml layout file.*/
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        // Make the {@link ListView} use the {@link CategoryAdapter} created above, so that the
        // {@link ListView} will display list items for each {@link Category} in the list.
        listView.setAdapter(adapter);

        listView.setScrollingCacheEnabled(false);
        // start SingleCategoryActivity activity for the button click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), SingleCategoryActivity.class);
                //parsing url for the activity
                intent.putExtra(getString(R.string.put_url), buildUrl(adapter.getItem(position).getTitle()));
                //parsing category type for the activity
                intent.putExtra(getString(R.string.put_type), adapter.getItem(position).getTitle());
                startActivity(intent);
            }
        });

        //return {@link View}
        return rootView;
    }

    /**
     * Build query for retrieve news data
     * * {@param type} is category type
     */
    private String buildUrl(String type) {

        // Base url for the query
        String BASE_URL = this.getString(R.string.base_url);
        Uri baseUri = Uri.parse(BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String pageSize = sharedPrefs.getString(
                getString(R.string.settings_page_size_key),
                getString(R.string.settings_page_size_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        uriBuilder.appendQueryParameter(this.getString(R.string.query), type);
        uriBuilder.appendQueryParameter(this.getString(R.string.page_size), pageSize);
        uriBuilder.appendQueryParameter(this.getString(R.string.show_fields), this.getString(R.string.fields));
        uriBuilder.appendQueryParameter(this.getString(R.string.show_references), this.getString(R.string.author_query));
        uriBuilder.appendQueryParameter(this.getString(R.string.order),orderBy);
        uriBuilder.appendQueryParameter(this.getString(R.string.api_key), this.getString(R.string.guarding_key));

        //return final query
        return uriBuilder.toString().replace(this.getString(R.string.replace), this.getString(R.string.comma));


    }

}
