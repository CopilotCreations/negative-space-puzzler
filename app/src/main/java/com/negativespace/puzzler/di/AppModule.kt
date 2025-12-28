package com.negativespace.puzzler.di

import android.content.Context
import com.negativespace.puzzler.data.local.GameStateDataStore
import com.negativespace.puzzler.data.local.SettingsDataStore
import com.negativespace.puzzler.data.repository.GameRepository
import com.negativespace.puzzler.data.repository.SettingsRepository
import com.negativespace.puzzler.domain.usecase.CheckSolutionUseCase
import com.negativespace.puzzler.domain.usecase.GenerateHintUseCase
import com.negativespace.puzzler.domain.usecase.ProcessTouchUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideSettingsDataStore(
        @ApplicationContext context: Context
    ): SettingsDataStore {
        return SettingsDataStore(context)
    }
    
    @Provides
    @Singleton
    fun provideGameStateDataStore(
        @ApplicationContext context: Context
    ): GameStateDataStore {
        return GameStateDataStore(context)
    }
    
    @Provides
    @Singleton
    fun provideSettingsRepository(
        settingsDataStore: SettingsDataStore
    ): SettingsRepository {
        return SettingsRepository(settingsDataStore)
    }
    
    @Provides
    @Singleton
    fun provideGameRepository(
        gameStateDataStore: GameStateDataStore
    ): GameRepository {
        return GameRepository(gameStateDataStore)
    }
    
    @Provides
    fun provideProcessTouchUseCase(): ProcessTouchUseCase {
        return ProcessTouchUseCase()
    }
    
    @Provides
    fun provideCheckSolutionUseCase(): CheckSolutionUseCase {
        return CheckSolutionUseCase()
    }
    
    @Provides
    fun provideGenerateHintUseCase(): GenerateHintUseCase {
        return GenerateHintUseCase()
    }
}
