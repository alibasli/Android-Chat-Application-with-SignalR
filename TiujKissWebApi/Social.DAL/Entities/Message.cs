using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Social.DAL.Entities
{
   public class Message
    {
        [Key, DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public Guid NotificationID { get; set; }

        public Guid FromUserID { get; set; }

        public Guid ToUserID { get; set; }

        public string Content { get; set; }

        public DateTime SendedDate { get; set; }

        public DateTime SeenDate { get; set; }

        public bool SeenStatus { get; set; }

        public DateTime DeliveredDate { get; set; }

        public bool DeliveredStatus { get; set; }

        public bool IsDeleted { get; set; }

        public virtual User User { get; set; }


    }
}
