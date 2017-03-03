package network.api;

import java.util.List;

import model.Newsbean;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by admin on 2017/3/3 0003.
 */

public interface News {

    @GET("news/AppHome/newslist/category/1/page/1.html")
    Observable<Newsbean> search();
}
