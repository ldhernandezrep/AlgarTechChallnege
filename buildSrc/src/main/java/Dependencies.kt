object Dependencies {

    val di = listOf(
        Dependency.Hilt,
        Dependency.Kapt.Hilt,
    )

    val common = listOf(
        Dependency.Coroutines,
        Dependency.CoreKtx
    )

    val remote = listOf(
        Dependency.Moshi,
        Dependency.MoshiAdapter,
        Dependency.MoshiKotlin,
        Dependency.RetrofitConverter,
        Dependency.Retrofit2,
        Dependency.Retrofit2GsonAdapter,
        Dependency.Retrofit2LoggingInterceptor,
    ).plus(common).plus(di)

    val app = listOf(
        Dependency.LiveData,
        Dependency.ViewModel,
        Dependency.AppCompat,
        Dependency.ConstraintLayout,
        Dependency.Palette,
        Dependency.NavigationFragment,
        Dependency.NavigationUI,
        Dependency.Glide,
        Dependency.Maps,
        Dependency.Places,
        Dependency.MapsUtilities
    ).plus(common).plus(di)

}