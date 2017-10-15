using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Web;

namespace Social.Areas.MobilApi
{
    public class PostsApiModel
    {
        public int PostId { get; set; }
        public int PostOwnerId { get; set; }
        public string PostOwnerNameSurname { get; set; }
        public string PostDateTime { get; set; }
        public byte[] PostOwnerProfileImage { get; set; }
        public string PostTitle { get; set; }
        public string PostBody { get; set; }
        public List<byte[]> PostImages { get; set; }
    }
    
}