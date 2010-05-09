// Copyright (c) 2002, 2003, 2004 by IBM Corporation
sa_gif    = "//stats.www.ibm.com/rc/images/uc.GIF";
sa_domain = "";
sa_ver    = "1.28";
sa_click="";s_7=document;s_k="undefined";function s_l(){s_3="";s_g="";s_4="SESSION_ID";s_5="";s_6=new Date();s_c=Math.random().toString().substring(2,10)+s_6.getTime().toString();if(s_4){s_1=s_7.cookie;if((s_9=s_1.indexOf(s_4))!=-1){s_h=s_9+s_4.length+1;s_8=s_1.indexOf(";",s_h);if(s_8==-1)s_8=s_1.length;s_5=escape(s_4+"="+s_1.substring(s_h,s_8));}}s_e=(typeof(screen)==s_k||screen==null)?'0x0x0':screen.colorDepth+"x"+screen.width+"x"+screen.height;s_b="?"+sa_ver+"&"+"&"+"&"+(sa_click?escape(sa_click):escape(location))+"&"+(sa_click?escape(location):escape(s_7.referrer))+"&"+s_5+"&"+"&"+"&"+s_6.getTimezoneOffset()+"&"+s_e+"&"+s_7.images.length.toString()+"&"+((typeof(SA_Title)==s_k||SA_Title==null)?"":escape(SA_Title))+"&"+((typeof(SA_Message)==s_k||SA_Message==null)?"":escape(SA_Message))+"&"+s_c;if(typeof(s_7.s_2)==s_k||s_7.s_2==null){s_7.s_2=new Image();}s_7.s_2.src=sa_gif+s_b;}
function sa_onclick(s_n,s_6){window.SA_Title=(typeof(SA_Title)==s_k||SA_Title==null)?"media_file":escape(SA_Title);sa_click=s_n;if(!(typeof(s_6)==s_k||s_6==null)){window.SA_Message=s_6;}if((navigator.appName.indexOf("Netscape")!=-1)){setTimeout("s_l()",0);}else{s_l();}return(true);}
if(typeof(s_7.s_2)==s_k||s_7.s_2==null){s_l();}
