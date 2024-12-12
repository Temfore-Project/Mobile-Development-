package com.capstone.temfore.dataimport androidx.lifecycle.LiveDataimport androidx.lifecycle.MutableLiveDataimport com.capstone.temfore.data.local.entity.FavoriteFoodimport com.capstone.temfore.data.local.room.FavoriteDaoimport com.capstone.temfore.data.remote.response.ListRecommendItemimport com.capstone.temfore.data.remote.retrofit.ApiServiceimport com.capstone.temfore.utils.AppExecutorsimport retrofit2.Callimport retrofit2.Callbackimport retrofit2.Responseclass RecommendationRepository(    private val apiService: ApiService,    private val favoriteDao: FavoriteDao,    private val appExecutors: AppExecutors,) {    val result = MutableLiveData<Result<ListRecommendItem>>()    fun insertFavoriteFood(id: Int): LiveData<Result<ListRecommendItem>> {        result.value = Result.Loading        val client = apiService.getFoodDetail(id)        client.enqueue(object : Callback<ListRecommendItem> {            override fun onResponse(call: Call<ListRecommendItem>, response: Response<ListRecommendItem>) {                if (response.isSuccessful) {                    val food = response.body()                    if (food != null) {                        // Simpan event ke database di thread background                        appExecutors.diskIO.execute {                            val newEvent = FavoriteFood(                                foodId = id.toString(),                                foodCategory = food.category ?: "",                                foodTitle = food.title ?: "",                                foodIngredients = food.ingredients ?: "",                                foodSteps = food.steps ?: "",                                foodType = food.type ?: "",                                foodTemp = food.tempCold ?: 0,                                foodImage = food.imageUrl ?: "",                            )                            favoriteDao.addFavoriteEvent(newEvent) // Menyimpan ke database                        }                    } else {                        result.value = Result.Error("Event data is empty")                    }                } else {                    result.value = Result.Error(response.message())                }            }            override fun onFailure(call: Call<ListRecommendItem>, t: Throwable) {                result.value = Result.Error(t.message.toString())            }        })        return result    }    fun isFoodFavorite(eventId: Int): LiveData<Boolean> {        return favoriteDao.isEventFavorite(eventId.toString())    }    fun removeFavoriteFoodById(id: Int) {        appExecutors.diskIO.execute {            favoriteDao.deleteFavoriteEventById(id.toString())        }    }    fun getAllFavoriteFood(): LiveData<List<FavoriteFood>> {        return favoriteDao.getAllFavoriteFood()    }    companion object {        @Volatile        private var instance: RecommendationRepository? = null        fun getInstance(            apiService: ApiService,            favoriteDao: FavoriteDao,            appExecutors: AppExecutors,        ): RecommendationRepository =            instance ?: synchronized(this) {                instance ?: RecommendationRepository(apiService, favoriteDao, appExecutors)            }.also { instance = it }    }}