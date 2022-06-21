package com.demo.architecture.dagger

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class, ActivityRetainedComponent::class)
@Module
class NavigationModule {

    private var cicerone: Cicerone<Router> = Cicerone.create(Router())

    @Provides
    fun provideAppRouter(): Router {
        return cicerone.router
    }

    @Provides
    internal fun provideNavigatorHolder(): NavigatorHolder {
        return cicerone.getNavigatorHolder()
    }

}
