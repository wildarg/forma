object versions {
    val agp = "4.1.0-rc01"
    val funktionale = "1.2"

    object jetbrains {
        val annotations = "20.0.0"
        val coroutines = "1.3.9"
        val kotlin = "1.4.0"
    }

    object androidx {
        val activity = "1.1.0"
        val annotation = "1.1.0"
        val arch = "2.1.0"
        val asynclayoutinflater = "1.0.0"
        val appcompat = "1.2.0"
        val cardview = "1.0.0"
        val collection = "1.0.0"
        val core = "1.3.1"
        val core_common = "2.1.0"
        val coordinatorlayout = "1.1.0"
        val constraintlayout = "2.0.1"
        val customview = "1.1.0"
        val cursoradapter = "1.0.0"
        val documentfile = "1.0.1"
        val drawerlayout = "1.1.0"
        val interpolator = "1.0.0"
        val fragment = "1.2.5"
        val legacy = "1.0.0"
        val lifecycle = "2.2.0"
        val loader = "1.1.0"
        val localbroadcastmanager = "1.0.0"
        val navigation = "2.3.0"
        val savedstate = "1.0.0"
        val slidingpanelayout = "1.0.0"
        val swiperefreshlayout = "1.0.0"
        val recyclerview = "1.1.0"
        val transition = "1.3.1"
        val vectordrawable = "1.1.0"
        val versionedparcelable = "1.1.0"
        val viewpager = "1.0.0"
    }

    object google {
        val material = "1.2.0"
    }

    object test {
        val espresso = "3.2.0"
        val junit = "4.12"
        val junit_ext = "1.1.1"
    }
}

object jetbrains {
    val annotations = "org.jetbrains:annotations:${versions.jetbrains.annotations}".dep
}

object kotlin {
    val stdlib_common = "org.jetbrains.kotlin:kotlin-stdlib-common:${versions.jetbrains.kotlin}".dep
    val stdlib = deps(
        "org.jetbrains.kotlin:kotlin-stdlib:${versions.jetbrains.kotlin}".dep,
        jetbrains.annotations,
        stdlib_common
    )
    val stdlib_jdk7 = deps(
        "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${versions.jetbrains.kotlin}".dep,
        stdlib
    )
    val stdlib_jdk8 = deps(
        "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${versions.jetbrains.kotlin}".dep,
        stdlib,
        stdlib_jdk7
    )
    val reflect = deps(
        "org.jetbrains.kotlin:kotlin-reflect:${versions.jetbrains.kotlin}".dep,
        stdlib
    )
}

object kotlinx {
    val coroutines_core = deps(
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${versions.jetbrains.coroutines}".dep,
        kotlin.stdlib
    )
    val coroutines_android = deps(
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${versions.jetbrains.coroutines}".dep,
        coroutines_core
    )
}

object androidx {
    val annotation = "androidx.annotation:annotation:${versions.androidx.annotation}".dep
    val collection = deps(
        annotation,
        "androidx.collection:collection:${versions.androidx.collection}".dep
    )
    val versionedparcelable = deps(
        "androidx.versionedparcelable:versionedparcelable:${versions.androidx.versionedparcelable}".dep,
        annotation,
        collection
    )
    val cardview = deps(
        "androidx.cardview:cardview:${versions.androidx.cardview}".dep,
        annotation
    )
    val core_common = deps(
        annotation,
        "androidx.arch.core:core-common:${versions.androidx.core_common}".dep
    )
    val lifecycle_common = deps(
        annotation,
        "androidx.lifecycle:lifecycle-common:${versions.androidx.lifecycle}".dep
    )
    val lifecycle_runtime = deps(
        "androidx.lifecycle:lifecycle-runtime:${versions.androidx.lifecycle}".dep,
        core_common,
        lifecycle_common
    )
    val lifecycle_viewmodel = deps(
        annotation,
        "androidx.lifecycle:lifecycle-viewmodel:${versions.androidx.lifecycle}".dep
    )

    val lifecycle_viewmodel_ktx = deps(
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${versions.androidx.lifecycle}".dep,
        lifecycle_viewmodel,
        kotlinx.coroutines_android
    )

    val lifecycle_runtime_ktx = deps(
        "androidx.lifecycle:lifecycle-runtime-ktx:${versions.androidx.lifecycle}".dep,
        annotation,
        kotlinx.coroutines_android
    )

    val savedstate = deps(
        "androidx.savedstate:savedstate:${versions.androidx.savedstate}".dep,
        annotation,
        core_common,
        lifecycle_common
    )

    val core_runtime = deps(
        "androidx.arch.core:core-runtime:${versions.androidx.arch}".dep,
        annotation,
        core_common
    )

    val lifecycle_livedate_core = deps(
        "androidx.lifecycle:lifecycle-livedata-core:${versions.androidx.lifecycle}".dep,
        core_common,
        core_runtime
    )

    val lifecycle_viewmodel_savedstate = deps(
        "androidx.lifecycle:lifecycle-viewmodel-savedstate:${versions.androidx.lifecycle}".dep,
        annotation,
        lifecycle_livedate_core,
        lifecycle_viewmodel,
        savedstate
    )

    val core = deps(
        "androidx.core:core:${versions.androidx.core}".dep,
        annotation,
        lifecycle_runtime,
        versionedparcelable
    )

    val core_ktx = deps(
        "androidx.core:core-ktx:${versions.androidx.core}".dep,
        annotation,
        core
    )

    val activity = deps(
        "androidx.activity:activity:${versions.androidx.activity}".dep,
        annotation,
        core,
        lifecycle_runtime,
        lifecycle_viewmodel,
        lifecycle_viewmodel_savedstate,
        savedstate
    )

    val loader = deps(
        "androidx.loader:loader:${versions.androidx.loader}".dep,
        annotation,
        collection,
        core,
        lifecycle_livedate_core,
        lifecycle_viewmodel
    )

    val customview = deps(
        "androidx.customview:customview:${versions.androidx.customview}".dep,
        annotation,
        core
    )

    val viewpager = deps(
        "androidx.viewpager:viewpager:${versions.androidx.viewpager}".dep,
        annotation,
        core,
        customview
    )

    val fragment = deps(
        "androidx.fragment:fragment:${versions.androidx.fragment}".dep,
        annotation,
        activity,
        collection,
        core,
        lifecycle_livedate_core,
        lifecycle_viewmodel,
        lifecycle_viewmodel_savedstate,
        loader,
        viewpager
    )

    val activity_ktx = deps(
        "androidx.activity:activity-ktx:${versions.androidx.activity}".dep,
        activity,
        core_ktx,
        lifecycle_runtime_ktx,
        lifecycle_viewmodel_ktx
    )

    val drawerlayout = deps(
        "androidx.drawerlayout:drawerlayout:${versions.androidx.drawerlayout}".dep,
        annotation,
        core,
        customview
    )

    val documentfile = deps(
        "androidx.documentfile:documentfile:${versions.androidx.documentfile}".dep,
        annotation
    )
    val localbroadcastmanager = deps(
        "androidx.localbroadcastmanager:localbroadcastmanager:${versions.androidx.localbroadcastmanager}".dep,
        annotation
    )
    val print = deps(
        "androidx.print:print:1.0.0".dep,
        annotation
    )
    val legacy_utils = deps(
        "androidx.legacy:legacy-support-core-utils:${versions.androidx.legacy}".dep,
        documentfile,
        localbroadcastmanager,
        print,
        annotation,
        core,
        loader
    )

    val vectordrawable = deps(
        "androidx.vectordrawable:vectordrawable:${versions.androidx.vectordrawable}".dep,
        annotation,
        collection,
        core
    )

    val asynclayoutinflater = deps(
        "androidx.asynclayoutinflater:asynclayoutinflater:${versions.androidx.asynclayoutinflater}".dep,
        annotation,
        core
    )

    val coordinatorlayout = deps(
        "androidx.coordinatorlayout:coordinatorlayout:${versions.androidx.coordinatorlayout}".dep,
        annotation,
        collection,
        core,
        customview
    )

    val interpolator = deps(
        "androidx.interpolator:interpolator:${versions.androidx.interpolator}".dep,
        annotation
    )
    val cursoradapter = deps(
        "androidx.cursoradapter:cursoradapter:${versions.androidx.cursoradapter}".dep,
        annotation
    )

    val slidingpanelayout = deps(
        "androidx.slidingpanelayout:slidingpanelayout:${versions.androidx.slidingpanelayout}".dep,
        annotation,
        customview
    )

    val swiperefreshlayout = deps(
        "androidx.swiperefreshlayout:swiperefreshlayout:${versions.androidx.swiperefreshlayout}".dep,
        annotation,
        core,
        interpolator
    )

    val legacy_ui = deps(
        "androidx.legacy:legacy-support-core-ui:${versions.androidx.legacy}".dep,
        asynclayoutinflater,
        coordinatorlayout,
        cursoradapter,
        interpolator,
        slidingpanelayout,
        swiperefreshlayout,
        core,
        customview,
        drawerlayout,
        legacy_utils,
        viewpager
    )
    val recyclerview = deps(
        "androidx.recyclerview:recyclerview:${versions.androidx.recyclerview}".dep,
        annotation,
        collection,
        core,
        customview
    )
    val transition = deps(
        "androidx.transition:transition:${versions.androidx.transition}".dep,
        annotation,
        collection,
        core,
        lifecycle_runtime
    )
    val navigation_common = deps(
        "androidx.navigation:navigation-common:${versions.androidx.navigation}".dep,
        annotation,
        collection,
        core
    )

    val appcompat = deps(
        "androidx.appcompat:appcompat:${versions.androidx.appcompat}".dep,
        "androidx.appcompat:appcompat-resources:${versions.androidx.appcompat}".dep,
        annotation,
        core,
        cursoradapter,
        drawerlayout,
        fragment,
        collection
    )
    val constraintlayout = deps(
        "androidx.constraintlayout:constraintlayout:${versions.androidx.constraintlayout}".dep,
        "androidx.constraintlayout:constraintlayout-solver:${versions.androidx.constraintlayout}".dep,
        appcompat,
        core
    )

    val navigation_ui_ktx = deps(
        "androidx.navigation:navigation-ui-ktx:${versions.androidx.navigation}",
        "androidx.navigation:navigation-ui:${versions.androidx.navigation}"
    )

    val navigation_common_ktx = deps(
        "androidx.navigation:navigation-common:${versions.androidx.navigation}".dep,
        annotation,
        collection,
        core
    )

    val navigation_runtime = deps(
        "androidx.navigation:navigation-runtime:${versions.androidx.navigation}".dep,
        activity,
        lifecycle_viewmodel,
        lifecycle_viewmodel_savedstate,
        navigation_common,
        savedstate
    )

    val navigation_runtime_ktx = deps(
        navigation_runtime,
        navigation_common_ktx,
        activity_ktx,
        lifecycle_viewmodel_ktx
    )

    val navigation_fragment = deps(
        "androidx.navigation:navigation-fragment:${versions.androidx.navigation}".dep,
        fragment,
        navigation_runtime
    )
    val navigation_fragment_ktx = deps(
        "androidx.navigation:navigation-fragment-ktx:${versions.androidx.navigation}".dep,
        navigation_fragment,
        navigation_runtime,
        fragment
    )
}

object google {
    val material = deps(
        "com.google.android.material:material:${versions.google.material}".dep,
        androidx.appcompat,
        androidx.cardview,
        androidx.core,
        androidx.annotation,
        androidx.legacy_ui,
        androidx.legacy_utils,
        androidx.recyclerview,
        androidx.transition
    )
}

object test {
    val junit = deps(
        "junit:junit:${versions.test.junit}".dep
    )

    val junit_ext = deps(
        "androidx.test.ext:junit:${versions.test.junit_ext}".dep
    )

    val espresso = deps(
        "androidx.test.espresso:espresso-core:${versions.test.espresso}".dep
    )
}