using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Social.Areas.MobilApi
{
    public class FeedbackModel
    {
        public int FeedId { get; set; }
        public string FeedBody { get; set; }
        public int OwnerId { get; set; }
        public string OwnerNameSurname { get; set; }
        public byte[] OwnerPhoto { get; set; }
        public int PostId { get; set; }
        public string PostTitle { get; set; }
        public string FeedDateTime { get; set; }
    }
}