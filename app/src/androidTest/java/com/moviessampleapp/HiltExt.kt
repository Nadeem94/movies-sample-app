package com.moviessampleapp

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import dagger.hilt.internal.Preconditions
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * This helper extension is needed to launch a fragment with Hilt Instrumentation tests:
 * <p>
 *     Hilt does not currently support FragmentScenario because there is no way to specify
 *     an activity class, and Hilt requires a Hilt fragment to be contained in a Hilt activity.
 *     One workaround for this is to launch a Hilt activity and then attach your fragment.
 * </p>
 * @see [https://dagger.dev/hilt/testing.html#:~:text=FooEntryPoint.class).getFoo()%3B%0A%7D-,Warning%3AHilt,-does%20not%20currently]
 */
@ExperimentalCoroutinesApi
inline fun <reified T : Fragment> launchFragmentInHiltContainer(
    fragmentArgs: Bundle? = null,
    themeResId: Int = R.style.FragmentScenarioEmptyFragmentActivityTheme,
    fragmentFactory: FragmentFactory? = null,
    crossinline action: T.() -> Unit = {}
) {
    val mainActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            HiltTestActivity::class.java
        )
    ).putExtra(
        "androidx.fragment.app.testing.FragmentScenario" +
                ".EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY", themeResId
    )

    ActivityScenario.launch<HiltTestActivity>(mainActivityIntent).onActivity { activity ->
        fragmentFactory?.let {
            activity.supportFragmentManager.fragmentFactory = it
        }
        val fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader),
            T::class.java.name
        )
        fragment.arguments = fragmentArgs

        activity.supportFragmentManager.beginTransaction()
            .add(android.R.id.content, fragment, "")
            .commitNow()

        (fragment as T).action()
    }
}
