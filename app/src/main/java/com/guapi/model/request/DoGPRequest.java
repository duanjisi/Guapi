package com.guapi.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * ╭╯☆★☆★╭╯
 * 　　╰╮★☆★╭╯
 * 　　　 ╯☆╭─╯
 * 　　 ╭ ╭╯
 * 　╔╝★╚╗  ★☆╮     BUG兄，没时间了快上车    ╭☆★
 * 　║★☆★║╔═══╗　╔═══╗　╔═══╗  ╔═══╗
 * 　║☆★☆║║★　☆║　║★　☆║　║★　☆║  ║★　☆║
 * ◢◎══◎╚╝◎═◎╝═╚◎═◎╝═╚◎═◎╝═╚◎═◎╝..........
 *
 * @author Jewel
 * @version 1.0
 * @since 2017/7/11 0011
 */

public class DoGPRequest {


    /**
     * status : 2000000
     * message : OK
     * data : {"value":"2"}
     */

    @SerializedName("gp_id")
    public String id;
    @SerializedName("do_type")
    public String type;
    @SerializedName("desc")
    public String desc;
    @SerializedName("to_comment_id")
    public String comment_id;
}
