### IDEA注册码生成算法

`public static String MakeKey(String name, int days, int id)`返回一个字符串就是注册码
`name`就是要注册的Name
`days`注册码过期时间
`id` 一个id标识，随便填
测试类中有一个测试，由于是静态方法，直接调用就可以生成注册码了。
