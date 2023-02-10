package com.demo.clear.conf

object LocalConf {
    var fullAdShowing=false
    var email="gerryg743kk@gmail.com"
    var agree="https://sites.google.com/view/tapbooster/home"
    var terms="https://sites.google.com/view/tapbooster/home"

    const val OPEN="tapo_open"
    const val CLEANING="tapo_in"
    const val HOME_NATIVE="tapo_nh"
    const val RESULT_NATIVE="tapo_re"


    const val localAd="""{
    "tapo_show":40,
    "tapo_click":10,
    "tapo_open":[
        {
            "tapo_type":"open",
            "tapo_plat":"admob",
            "tapo_id":"ca-app-pub-3940256099942544/3419835294xxx",
            "tapo_prio":3
        },
        {
            "tapo_type":"itn",
            "tapo_plat":"admob",
            "tapo_id":"ca-app-pub-3940256099942544/1033173712",
            "tapo_prio":1
        },
        {
            "tapo_type":"open",
            "tapo_plat":"admob",
            "tapo_id":"ca-app-pub-3940256099942544/3419835294",
            "tapo_prio":2
        }
    ],
    "tapo_in":[
        {
            "tapo_type":"itn",
            "tapo_plat":"admob",
            "tapo_id":"ca-app-pub-3940256099942544/1033173712",
            "tapo_prio":1
        },
        {
            "tapo_type":"itn",
            "tapo_plat":"admob",
            "tapo_id":"ca-app-pub-3940256099942544/8691691433",
            "tapo_prio":2
        },
        {
            "tapo_type":"itn",
            "tapo_plat":"admob",
            "tapo_id":"ca-app-pub-3940256099942544/1033173712xxx",
            "tapo_prio":3
        }
    ],
    "tapo_nh":[
        {
            "tapo_type":"nti",
            "tapo_plat":"admob",
            "tapo_id":"ca-app-pub-3940256099942544/2247696110",
            "tapo_prio":2
        },
        {
            "tapo_type":"nti",
            "tapo_plat":"admob",
            "tapo_id":"ca-app-pub-3940256099942544/2247696110xxx",
            "tapo_prio":3
        },
        {
            "tapo_type":"nti",
            "tapo_plat":"admob",
            "tapo_id":"ca-app-pub-3940256099942544/1044960115",
            "tapo_prio":1
        }
    ],
    "tapo_re":[
        {
            "tapo_type":"nti",
            "tapo_plat":"admob",
            "tapo_id":"ca-app-pub-3940256099942544/2247696110",
            "tapo_prio":1
        },
        {
            "tapo_type":"nti",
            "tapo_plat":"admob",
            "tapo_id":"ca-app-pub-3940256099942544/1044960115",
            "tapo_prio":2
        },
        {
            "tapo_type":"nti",
            "tapo_plat":"admob",
            "tapo_id":"ca-app-pub-3940256099942544/2247696110xx",
            "tapo_prio":3
        }
    ]
}"""
}