using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Social.DAL.Entities
{
   public class Notification
    {
        [Key, DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public Guid NotificationID { get; set; }

        public Guid SharingID { get; set; }

        public Guid FromUserID { get; set; }

        public Guid ToUserID { get; set; }

        public DateTime DateSending { get; set; }

        public virtual User User { get; set; }

    }
}
