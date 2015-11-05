(ns alpaca-traders.about
  (:require [clojure.string :as str]))

(def body-text 
  "Cake tootsie roll wafer marshmallow jelly beans toffee cake muffin dragée. Chocolate bar chocolate cupcake. Marzipan dragée bear claw tart gummies icing chocolate. Caramels candy canes candy canes cotton candy. Lollipop topping chocolate lollipop dessert gummi bears donut. Soufflé topping candy brownie gummies caramels. Sweet donut bear claw candy caramels cake bear claw danish cookie. Lollipop marzipan danish tart powder. Sweet chocolate jelly beans chupa chups carrot cake gummi bears gingerbread. Lollipop chocolate cake muffin candy canes lollipop chupa chups. Tiramisu icing sweet. Cookie tootsie roll chupa chups cotton candy gingerbread gummies cookie.
  
  Croissant cotton candy oat cake. Croissant pudding jujubes macaroon. Candy jelly beans biscuit. Gingerbread macaroon liquorice tootsie roll liquorice chupa chups apple pie. Bear claw liquorice biscuit. Sweet dragée icing icing marshmallow. Marzipan soufflé gingerbread cotton candy candy canes candy. Cotton candy powder marzipan jujubes dessert cotton candy sesame snaps. Sweet bonbon macaroon. Pie halvah pie sweet roll tart sesame snaps icing. Cake sesame snaps pudding icing cotton candy. Caramels donut lemon drops halvah candy canes sugar plum. Cake candy chupa chups jujubes oat cake pudding oat cake.
  
  Cupcake fruitcake pastry. Bonbon jelly-o sweet. Dessert jujubes ice cream chocolate bar macaroon jelly wafer. Cotton candy tart candy dragée marzipan gummi bears oat cake cookie. Cake chocolate cake sugar plum pastry candy canes. Soufflé toffee cheesecake chocolate macaroon lemon drops pudding gummi bears dragée. Soufflé sugar plum lemon drops pudding. Biscuit gingerbread jelly. Marzipan cake croissant. Candy carrot cake icing bear claw ice cream lemon drops sesame snaps soufflé. Jujubes ice cream dragée gingerbread lemon drops. Chocolate bar gummi bears tiramisu donut donut. Apple pie croissant dessert powder liquorice gummi bears sesame snaps carrot cake croissant.
  
  Dessert tootsie roll apple pie sweet roll cupcake. Tart candy canes topping. Lollipop croissant pie soufflé. Tiramisu biscuit soufflé lollipop tootsie roll. Caramels biscuit pudding marshmallow cupcake powder halvah. Liquorice macaroon gummies cookie biscuit. Halvah chocolate cake caramels croissant cotton candy. Jelly candy canes marshmallow candy canes topping liquorice brownie. Oat cake marshmallow muffin sweet roll cake toffee macaroon chocolate cake. Chupa chups liquorice chocolate cake pudding jelly beans marshmallow. Bonbon topping cheesecake topping soufflé. Powder danish pastry cupcake pudding. Chocolate jelly-o danish gummi bears soufflé cake jelly-o candy canes.
  
  Candy macaroon marshmallow. Candy canes pastry fruitcake. Powder caramels wafer caramels powder. Gummi bears candy gingerbread gummi bears. Donut fruitcake jujubes biscuit bonbon bonbon jelly beans. Pudding cupcake sugar plum. Donut cake chocolate cotton candy tootsie roll caramels. Bonbon jelly wafer. Soufflé powder sweet lollipop. Muffin dragée sweet roll muffin wafer. Cotton candy bonbon pastry jelly candy pastry cake jelly-o dragée. Icing gummies marzipan pastry biscuit jujubes chocolate bar. Tiramisu cheesecake pastry lollipop apple pie jelly cotton candy ice cream.")

(defn break-into-paragraphs [text]
  (let [paragraphs (remove 
                     str/blank?
                     (str/split text "\n"))]
    (map 
      #(do 
         [:p {:key %} %])
      paragraphs)))

(defn view []
  [:div.default-body 
   [:h1 "What's this whole thing about?"]
   (break-into-paragraphs body-text)])