var _viewer = this;

//开始时间和结束时间之间的联动
rhDate.compareTwoDate(_viewer.form.getItem("ADD_BEGTIME").obj, _viewer.form.getItem("ADD_ENDTIME").obj, "yyyy-MM-dd HH:mm");