package team_emergensor.co.jp.emergensor.presentation.home

import android.app.Fragment
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import team_emergensor.co.jp.emergensor.R
import team_emergensor.co.jp.emergensor.databinding.FragmentNewsFeedBinding

class NewsFeedFragment : Fragment() {

    private lateinit var binding: FragmentNewsFeedBinding

    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_feed, container, false)
        return binding.root
    }
}
