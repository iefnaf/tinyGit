## Git的本质

> 本文根据cs61b 2021 lec12整理而来。

我们都知道，Git是一个分布式**版本控制**系统，所以在了解Git之前我们需要先搞清楚什么是版本控制。

Google Docs支持对文档进行版本控制，我们先来看一下Google Docs的版本控制功能。

我们在使用Google Docs写文章时，Google Docs会每隔一段时间保存文档的一个版本，通过查看所有的历史版本，我们可以看到整个文档的编写过程。

<img src="https://github.com/iefnaf/tinyGit/blob/master/docs/images/image-20220507165518569.png" alt="image-20220507165518569" style="zoom:50%;" />

软件开发是一个迭代的过程，保存代码的不同版本是很有必要的。和Google Docs一样，Git所做的事情就是**保存代码的不同版本**，方便我们在不同的版本之间进行切换。

如果没有Git，我们要怎么保存代码的不同版本呢？

一个非常简单的做法是每当我们需要保存一个版本时都把整个文件夹拷贝一份。

<img src="https://github.com/iefnaf/tinyGit/blob/master/docs/images/image-20220507170826149.png" alt="image-20220507170826149" style="zoom: 50%;" />

这种做法的优点是实现起来非常简单，我们仅仅需要拷贝文件就行了。

但是缺点也非常明显：

* 浪费存储空间（不同版本之间有很多文件是相同的）
* 需要时刻将备份牢记在心
* 合并不同的版本非常麻烦

尽管这种做法有各种各样的缺点，听起来很不靠谱，但是这就是Git的本质。

**Git的本质是复制文件**。

每当你改完一个文件并提交一个commit时，Git都会将整个工程的一份拷贝保存在一个隐藏的名为.git的文件夹中。

但是如何避免保存重复的文件，节省存储空间呢？

我们不妨穿越到2005年，假设我们就是Linus，那么我们应该如何设计Git从而避免文件重复呢？

首先假设这样一个场景：我们在一个Java的项目中提交了三个commit。

* 在V1中，创建了一个readme.txt文件
* 在V2中，创建了utils/Utils.java、game/Game.java和game/Tets.java三个文件，并且修改了readme.txt的内容
* 在V3中，修改了game/Game.java，并且将readme.txt改回了V1的版本。

<img src="https://github.com/iefnaf/tinyGit/blob/master/docs/images/image-20220507191701564.png" alt="image-20220507191701564" style="zoom:50%;" />

以下是我们实现Git的几种方法。

## 实现1：拷贝整个项目

如前面所说，每次提交commit时，Git将整个项目复制一份，保存在.git文件夹中。

使用这种方法，在提交3个commit之后我们的.git文件夹中应该包含以下这些文件：

<img src="https://github.com/iefnaf/tinyGit/blob/master/docs/images/image-20220507191957053.png" alt="image-20220507191957053" style="zoom:50%;" />

这种做法的优点是实现简单：

* 每次用户提交一个commit，git创建一个新的文件夹，并将整个项目复制到该文件夹中。
* 当用户使用checkout命令时，git将当前工作目录的文件全部删除，并从指定版本的文件夹中复制文件到工作目录中。

但是缺点也是显而易见的：

* 保存了两个完全相同的Utils.java、Test.java和readme.txt

## 实现2：保存内容发生改变的文件

我们需要在实现1的基础上避免保存冗余的文件，一个简单的想法是，对于一个文件的不同版本，我们只保存一份。

在消除了冗余文件之后，我们的三个commit和项目中文件的关系应该看起来像下图这样：

<img src="https://github.com/iefnaf/tinyGit/blob/master/docs/images/image-20220507192459329.png" alt="image-20220507192459329" style="zoom:50%;" />

此时，我们的.git文件夹中应该包含以下文件：

<img src="https://github.com/iefnaf/tinyGit/blob/master/docs/images/image-20220507192650851.png" alt="image-20220507192650851" style="zoom:50%;" />

相比于方法1，方法2的优点是节省了存储空间，但是checkout命令也变得更复杂了，尤其是当我们想checkout到一个commit时，我们需要从不同的版本目录中拷贝文件到工作目录。

下面是方法2checkout的一个例子。假设我们有以下五个commit：

<img src="https://github.com/iefnaf/tinyGit/blob/master/docs/images/image-20220507193050585.png" alt="image-20220507193050585" style="zoom:50%;" />

现在我们需要从v5切到v4，git为了得到需要拷贝的文件，不得不扫描从v1到v4的所有commit的历史。

<img src="https://github.com/iefnaf/tinyGit/blob/master/docs/images/image-20220507193443215.png" alt="image-20220507193443215" style="zoom:50%;" />

如何做才能避免这种扫描的过程呢？换个问法，我们如何才能快速得到某个commit中所有的文件，以及每个文件的版本？

## 实现3：在方法2的基础上增加版本

保存所有的commit，每个commit是一个map，记录这个commit中项目所包含的文件和该文件对应的版本，并使用文件所在的commit作为该文件的版本号。

<img src="https://github.com/iefnaf/tinyGit/blob/master/docs/images/image-20220507193628549.png" alt="image-20220507193628549" style="zoom:50%;" />

以上图为例，我们需要保存五个commit，每个commit的内容如下所示：

``` text
V1: Hello.java -> v1
V2: Hello.java -> v2, Friend.java -> v2
V3: Hello.java -> v2, Friend.java -> v2, Egg.java -> v3
V4: Hello.java -> v2, Friend.java -> v4, Egg.java -> v3
V5: Hello.java -> v5, Friend.java -> v4, Egg.java -> v3
```

此时，如果我们想切到V4，通过查看V4 commit中的内容，就可以立刻知道，Hello.java需要从v2文件夹中拷贝，Friend.java需要从v4文件夹中拷贝，Egg.java需要从v3文件夹中拷贝。

## 实现4：使用时间作为版本号

方法3已经做到了消除冗余文件，但是还不是git最终所采用的方法。

我们说git是分布式的版本控制软件，其中的分布式体现在它能够支持多个人同时向一个项目提交commit。方法3的问题就在于没有考虑分布式的场景。

假设有两个程序员，同时从v3开始开发，并分别提交了一个commit，那么他们谁提交的commit会成为v4呢？可以按照他们提交commit的时间先后顺序来决定吗？

显然是不行的，因为git是一个分布式的应用，没有中心化的服务器来对两个commit进行排序。

因为存在着上述问题，所以我们无法使用方法3中递增版本号的方法来对commit进行编号。

那么能不能使用提交commit时的本地时间来作为commit的编号呢？就像下图这样？

<img src="https://github.com/iefnaf/tinyGit/blob/master/docs/images/image-20220507195739967.png" alt="image-20220507195739967" style="zoom:50%;" />

这种做法同样是行不通的，因为git是一个分布式应用，可能存在两个程序员同时提交commit的情形，这种情况下两个commit的编号就发生冲突了。

现在问题变为，如何对commit进行编号以避免不同的commit编号之间发生冲突？

## 实现5：使用哈希值作为版本号

最终答案是使用哈希。

我们知道，我们可以对一个文件的内容取哈希，得到一个固定位数的数字。

比如我们对下面这段代码使用git-SHA1取哈希，可以得到一个160bit的数字。

``` java
public class HelloWorld {
   public static void main(String[] args) {
   	System.out.println("Hello World!");
   }
}
```

``` text
66ccdc645c9d156d5c796dbe6ed768430c1562a2
```

哈希函数能够向我们保证，如果相同的内容取哈希得到的数字相同，不同的内容取哈希的到的数字不同（相同的概率小于地球爆炸）。

## Git的做法

git存储所有的commit，每个commit包含以下内容：

* 提交该commit的作者
* 提交该commit的时间
* 一段关于该commit的简要描述
* 该commit的父commit的编号
* 该commit中包含的所有文件的列表，以及每个文件对应的版本号

<img src="https://github.com/iefnaf/tinyGit/blob/master/docs/images/image-20220507200816780.png" alt="image-20220507200816780" style="zoom:50%;" />

我们在查看commit的历史记录时，每个commit后会有一堆数字，这个数字就是对该commit的内容取哈希的值，也是这个commit的编号。
