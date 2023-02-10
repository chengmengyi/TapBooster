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
  "tapo_show": 50,
  "tapo_click": 15,
  "tapo_open": [
    {
      "tapo_type": "open",
      "tapo_plat": "admob",
      "tapo_id": "ca-app-pub-6337191878285963/6522162816",
      "tapo_prio": 1
    }
  ],
  "tapo_in": [
    {
      "tapo_type": "itn",
      "tapo_plat": "admob",
      "tapo_id": "ca-app-pub-6337191878285963/5344812514",
      "tapo_prio": 1
    }
  ],
  "tapo_nh": [
    {
      "tapo_type": "nti",
      "tapo_plat": "admob",
      "tapo_id": "ca-app-pub-6337191878285963/6877386031",
      "tapo_prio": 1
    }
  ],
  "tapo_re": [
    {
      "tapo_type": "nti",
      "tapo_plat": "admob",
      "tapo_id": "ca-app-pub-6337191878285963/4251222693",
      "tapo_prio": 1
    }
  ]
}"""
}