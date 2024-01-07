package com.example.madtest1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import java.util.List;

public class NewsDesign extends Fragment implements NewsAdapter.OnItemClickListener {

    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_design, container, false);

        // Load articles into TextView elements
        TextView article1TextView = view.findViewById(R.id.article1_text_view);
        article1TextView.setText("Food Waste: A Global Challenge Unveiled\n\n" +
                "Food waste has emerged as a profound global predicament, with a staggering one-third of the world's produced food—equivalent to approximately 1.3 billion tons—being discarded annually. This not only signifies a wasteful depletion of vital resources such as water, land, and energy but also contributes to environmental degradation by releasing methane, a potent greenhouse gas, during the decomposition process. The causes of food waste are multifaceted, encompassing inefficient farming practices, confusion stemming from expiration dates, and adherence to cosmetic standards that prematurely lead to discarding. Consumer behaviors, including overbuying and inadequate food storage, further exacerbate this issue.");
        TextView article2TextView = view.findViewById(R.id.article2_text_view);
        article2TextView.setText("The Meat Paradox: Striking a Balance for a Sustainable Planet\n\n" +
                "The intricate relationship between meat consumption and environmental threats unfolds in the meat paradox, where the pivotal role of meat production in providing protein and livelihoods collides with the environmental toll of large-scale industrialization. This industrialization fuels deforestation, contributes to climate change through methane emissions, and places a considerable strain on water resources. A conscientious approach to meat consumption is proposed, advocating for alternatives such as reducing overall meat intake, choosing sustainably raised meat from animals with natural diets, and exploring plant-based protein sources like beans, lentils, and tofu.");

        // Initialize RecyclerView and fetch news data
        newsRecyclerView = view.findViewById(R.id.news_recycler_view);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getNews();

        return view;
    }

    void getNews() {
        NewsApiClient newsApiClient = new NewsApiClient("812ce3215d314bbaa78cba205bc92e06");
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .q("health")
                        .language("en")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        if (isAdded()) { // Check if fragment is currently added to its activity
                            updateUI(response.getArticles());
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        // Handle failure
                    }
                }
        );
    }

    private void updateUI(List<Article> articles) {
        if (getContext() != null) {
            // Initialize the adapter with the click listener
            newsAdapter = new NewsAdapter(articles, this);
            // Set the adapter for the RecyclerView
            newsRecyclerView.setAdapter(newsAdapter);
        }
    }

    @Override
    public void onItemClick(Article article) {
        // Open the full news article in a web browser
        if (getActivity() != null && article.getUrl() != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getUrl()));
            startActivity(intent);
        }
    }
}
