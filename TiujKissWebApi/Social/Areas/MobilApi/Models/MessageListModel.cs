using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Social.Areas.MobilApi
{
    public class MessageListModel
    {
        public int SenderId { get; set; }
        public string SenderNameSurname { get; set; }
        public byte[] SenderPhoto { get; set; }
        public bool MessageRead { get; set; }
    }
}