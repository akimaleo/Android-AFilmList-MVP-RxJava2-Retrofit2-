package com.letoti.kawa.philmaker.list

import com.letoti.kawa.philmaker.common.IView
import com.letoti.kawa.philmaker.web.entity.MovieDto

/**
 * Created by kawa on 29.01.2018.
 */

interface MovieListView : IView {
    fun showMovieList(item: MovieDto)
}