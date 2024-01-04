object Dependencies {

    val di = listOf(
        Dependency.Hilt,
        Dependency.Kapt.Hilt,
    )

    val common = listOf(
        Dependency.Coroutines,
        Dependency.CoreKtx
    )

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