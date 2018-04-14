package com.sky.chowder.ui.activity

import android.Manifest
import android.content.ActivityNotFoundException
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.sky.SkyApp
import com.sky.base.BasePActivity
import com.sky.chowder.R
import com.sky.chowder.api.view.IMainView
import com.sky.chowder.model.ActivityModel
import com.sky.chowder.ui.adapter.MainAdapter
import com.sky.chowder.ui.presenter.MainP
import com.sky.utils.AppUtils
import com.sky.utils.JumpAct
import com.sky.utils.LogUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File

/**
 * Created by SKY on 2015/12/6.
 * 主页
 */
class MainActivity : BasePActivity<MainP>(), Toolbar.OnMenuItemClickListener, IMainView {
//    override fun onCreate(savedInstanceState: Bundle?) {
//super.onCreate(savedInstanceState)
//Debug.startMethodTracing()
//    }
//    override fun onDestroy() {
//super.onDestroy()
//Debug.stopMethodTracing()
//    }

    private lateinit var adapter: MainAdapter
    public override fun getLayoutResId(): Int = R.layout.activity_main
    override fun creatPresenter() = MainP(this)
    public override fun initialize() {
        baseTitle.setLeftButton(-1)
        recycler?.setHasFixedSize(true)
        adapter = MainAdapter(R.layout.adapter_main)
        recycler.adapter = adapter

        adapter?.setOnItemClickListener { _, position -> JumpAct.jumpActivity(this, adapter.datas[position].componentName) }
        adapter?.setOnItemLongClickListener { _, _ ->
            showToast("长按监听已处理")
            true
        }
        AppUtils.isPermissions(this,
                arrayOf(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE),
                intArrayOf(0, 0, 0)
        )
//        registerOnClick(fab,fab1)
        fab1.setOnClickListener {
            var text = getString(R.string.cezi).trim().replace(" ", "")
            LogUtils.i("总长==${text.length}")
        }
        fab.setOnClickListener {
            //            七发
//            两汉：枚乘
//            　　楚太子有疾，而吴客往问之，曰：“伏闻太子玉体不安，亦少间乎？”太子曰：“惫！谨谢客。”客因称曰：“今时天下安宁，四宇和平，太子方富于年。意者久耽安乐，日夜无极，邪气袭逆，中若结轖。纷屯澹淡，嘘唏烦酲，惕惕怵怵，卧不得瞑。虚中重听，恶闻人声，精神越渫，百病咸生。聪明眩曜，悦怒不平。久执不废，大命乃倾。太子岂有是乎？”太子曰：“谨谢客。赖君之力，时时有之，然未至于是也”。”客曰：“今夫贵人之子，必宫居而闺处，内有保母，外有傅父，欲交无所。饮食则温淳甘膬，脭醲肥厚；衣裳则杂遝曼暖，燂烁热暑。虽有金石之坚，犹将销铄而挺解也，况其在筋骨之间乎哉？故曰：纵耳目之欲，恣支体之安者，伤血脉之和。且夫出舆入辇，命曰蹶痿之机；洞房清官，命曰寒热之媒；皓齿蛾眉，命曰伐性之斧；甘脆肥脓，命曰腐肠之药。今太子肤色靡曼，四支委随，筋骨挺解，血脉淫濯，手足堕窳；越女侍前，齐姬奉后；往来游醼，纵恣于曲房隐间之中。此甘餐毒药，戏猛兽之爪牙也。所从来者至深远，淹滞永久而不废，虽令扁鹊治内，巫咸治外，尚何及哉！今如太子之病者，独宜世之君子，博见强识，承间语事，变度易意，常无离侧，以为羽翼。淹沈之乐，浩唐之心，遁佚之志，其奚由至哉！’’太子曰：“诺。病已，请事此言。”
//            　　客曰：“今太子之病，可无药石针刺灸疗而已，可以要言妙道说而去也。不欲闻之乎？”太子曰：“仆愿闻之。”
//            　　客曰：“龙门之桐，高百尺而无枝。中郁结之轮菌，根扶疏以分离。上有千仞之峰，下临百丈之溪。湍流遡波，又澹淡之。其根半死半生。冬则烈风漂霰、飞雪之所激也，夏则霄霆、霹雳之所感也。朝则鹂黄、鳱鴠鸣焉，暮则羁雌、迷鸟宿焉。独鹄晨号乎其上，鹍鸡哀鸣翔乎其下。于是背秋涉冬，使琴挚斫斩以为琴，野茧之丝以为弦，孤子之钩以为隐，九寡之珥以为约。使师堂操《畅》，伯子牙为之歌。歌曰：‘麦秀蔪兮雉朝飞，向虚壑兮背槁槐，依绝区兮临回溪。’飞鸟闻之，翕翼而不能去；野兽闻之，垂耳而不能行；蚑、蟜、蝼、蚁闻之，柱喙而不能前。此亦天下之至悲也，太子能强起听之乎？”太子曰：“仆病未能也。”
//            　　客曰：“犓牛之腴，菜以笋蒲。肥狗之和，冒以山肤。楚苗之食，安胡之飰，抟之不解，一啜而散。于是使伊尹煎熬，易牙调和。熊蹯之胹，芍药之酱。薄耆之炙，鲜鲤之鱠。秋黄之苏，白露之茹。兰英之酒，酌以涤口。山梁之餐，豢豹之胎。小飰大歠，如汤沃雪。此亦天下之至美也，太子能强起尝之乎？”太子曰：“仆病未能也。”
//            　　客曰：“钟、岱之牡，齿至之车，前似飞鸟，后类距虚。穱麦服处，躁中烦外。羁坚辔，附易路。于是伯乐相其前后，王良、造父为之御，秦缺、楼季为之右。此两人者，马佚能止之，车覆能起之。于是使射千镒之重，争千里之逐。此亦天下之至骏也，太子能强起乘之乎？”太子曰：“仆病未能也。”
//            　　客曰：“既登景夷之台，南望荆山，北望汝海，左江右湖，其乐无有。于是使博辩之士，原本山川，极命草木，比物属事，离辞连类。浮游览观，乃下置酒于虞杯之宫。连廊四注，台城层构，纷纭玄绿。辇道邪交，黄池纡曲。溷章、白鹭，孔鸟、鶤鹄，鵷雏、鵁鶄，翠鬣紫缨。螭龙、德牧，邕邕群鸣。阳鱼腾跃，奋翼振鳞。漃漻薵蓼，蔓草芳苓。女桑、河柳，素叶紫茎。苗松、豫章，条上造天。梧桐、并阊，极望成林。众芳芬郁，乱于五风。从容猗靡，消息阳阴。列坐纵酒，荡乐娱心。景春佐酒，杜连理音。滋味杂陈，肴糅错该。练色娱目，流声悦耳。于是乃发《激楚》之结风，扬郑、卫之皓乐。使先施、徵舒、阳文、段干、吴娃、闾娵、傅予之徒，杂裾垂髾，目窕心与。揄流波，杂杜若，蒙清尘，被兰泽，嬿服而御。此亦天下之靡丽、皓侈、广博之乐也，太子能强起游乎？太子曰：“仆病未能也。”
//            　　客曰：“将为太子驯骐骥之马，驾飞軨之舆，乘牡骏之乘。右夏服之劲箭，左乌号之雕弓。游涉乎云林，周驰乎兰泽，弭节乎江浔。掩青苹，游清风。陶阳气，荡春心。逐狡兽，集轻禽。于是极犬马之才，困野兽之足，穷相御之智巧，恐虎豹，慴鸷鸟。逐马鸣镳，鱼跨麋角。履游麕兔，蹈践麖鹿，汗流沫坠，寃伏陵窘。无创而死者，固足充后乘矣。此校猎之至壮也，太子能强起游乎？”太子曰：“卜病未能也。”然阳气见于眉宇之间，侵淫而上，几满大宅。
//            　　客见太子有悦色，遂推而进之曰：“冥火薄天，兵车雷运，旌旗偃蹇，羽毛肃纷。驰骋角逐，慕味争先。徼墨广博，观望之有圻；纯粹全牺，献之公门。太子曰：“善！愿复闻之。”
//            　　客曰：“未既。于是榛林深泽，烟云闇莫，兕虎并作。毅武孔猛，袒裼身薄。白刃磑磑，矛戟交错。收获掌功，赏赐金帛。掩苹肆若，为牧人席。旨酒嘉肴，羞炰脍灸，以御宾客。涌觞并起，动心惊耳。诚不必悔，决绝以诺；贞信之色，形于金石。高歌陈唱，万岁无斁。此真太子之所喜也，能强起而游乎？”太子曰：“仆甚愿从，直恐为诸大夫累耳。”然而有起色矣。
//            　　客曰：“将以八月之望，与诸侯远方交游兄弟，并往观涛乎广陵之曲江。至则未见涛之形也，徒观水力之所到，则恤然足以骇矣。观其所驾轶者，所擢拔者，所扬汩者，所温汾者，所涤汔者，虽有心略辞给，固未能缕形其所由然也。怳兮忽兮，聊兮栗兮，混汩汩兮，忽兮慌兮，俶兮傥兮，浩瀇瀁兮，慌旷旷兮。秉意乎南山，通望乎东海。虹洞兮苍天，极虑乎崖涘。流揽无穷，归神日母。汩乘流而下降兮，或不知其所止。或纷纭其流折兮，忽缪往而不来。临朱汜而远逝兮，中虚烦而益怠。莫离散而发曙兮，内存心而自持。于是澡概胸中，洒练五藏，澹澉手足，頮濯发齿。揄弃恬怠，输写淟浊，分决狐疑，发皇耳目。当是之时，虽有淹病滞疾，犹将伸伛起躄，发瞽披聋而观望之也，况直眇小烦懑，酲醲病酒之徒哉！故曰：发蒙解惑，不足以言也。”太子曰：“善，然则涛何气哉？”
//            　　客曰：“不记也。然闻于师曰，似神而非者三：疾雷闻百里；江水逆流，海水上潮；山出内云，日夜不止。衍溢漂疾，波涌而涛起。其始起也，洪淋淋焉，若白鹭之下翔。其少进也，浩浩溰溰，如素车白马帷盖之张。其波涌而云乱，扰扰焉如三军之腾装。其旁作而奔起也，飘飘焉如轻车之勒兵。六驾蛟龙，附从太白。纯驰皓蜺，前后络绎。顒顒卬卬，椐椐强强，莘莘将将。壁垒重坚，沓杂似军行。訇隐匈磕，轧盘涌裔，原不可当。观其两旁，则滂渤怫郁，闇漠感突，上击下律，有似勇壮之卒，突怒而无畏。蹈壁冲津，穷曲随隈，逾岸出追。遇者死，当者坏。初发乎或围之津涯，荄轸谷分。回翔青篾，衔枚檀桓。弭节伍子之山，通厉骨母之场，凌赤岸，篲扶桑，横奔似雷行，诚奋厥武，如振如怒，沌沌浑浑，状如奔马。混混庉庉，声如雷鼓。发怒庢沓，清升逾跇，侯波奋振，合战于藉藉之口。鸟不及飞，鱼不及回，兽不及走。纷纷翼翼，波涌云乱，荡取南山，背击北岸。覆亏丘陵，平夷西畔。险险戏戏，崩坏陂池，决胜乃罢。瀄汩潺湲，披扬流洒。横暴之极，鱼鳖失势，颠倒偃侧，沋沋湲湲，蒲伏连延。神物恠疑，不可胜言。直使人踣焉，洄闇凄怆焉。此天下恠异诡观也，太子能强起观之乎？”太子曰：“仆病未能也。”
//            　　客曰：“将为太子奏方术之士有资略者，若庄周、魏牟、杨朱、墨翟、便蜎、詹何之伦，使之论天下之精微，理万物之是非。孔、老览观，孟子筹之，万不失一。此亦天下要言妙道也，太子岂欲闻之乎？”
//            　　于是太子据几而起，曰：“涣乎若一听圣人辩士之言。”涊然汗出，霍然病已。
        }
    }

    //    private fun registerOnClick(vararg views: View) {
//        for (v in views)
//            v.setOnClickListener(this)
//    }

    override fun setData(data: List<ActivityModel>) {
        adapter?.datas = data
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {//跳转到指定的别的APP的activity
            R.id.action_01 -> startOther("com.cx.architecture", "com.cx.architecture.ui.activity.LoginActivity")
            R.id.action_02 -> startOther("com.gc.materialdesigndemo", "com.gc.materialdesigndemo.ui.MainActivity")
        }
        return false
    }

    private fun startOther(packageName: String, componentName: String) {
        try {
            JumpAct.jumpActivity(this@MainActivity, packageName, componentName)
        } catch (e: ActivityNotFoundException) {
            showToast("程序未安装")
        }
    }

    private var lastBack: Long = 0
    override fun onBackPressed() {
        val now = System.currentTimeMillis()
        if (now - lastBack > 3000) {
            showToast(getString(R.string.toast_exit))
            lastBack = now
        } else super.onBackPressed()
    }

    /**
     * 文件属性
     */
    private fun fileTest() {
        val f = File(SkyApp.getInstance().fileCacheDir + "pass.txt")
        System.out.println(f.getParent())//返回此抽象路径名父目录的路径名字符串；如果此路径名没有指定父目录，则返回 null
        System.out.println(f.getName())//返回由此抽象路径名表示的文件或目录的名称
        System.out.println(f.exists())//测试此抽象路径名表示的文件或目录是否存在
        System.out.println(f.getAbsoluteFile())// 返回此抽象路径名的绝对路径名形式
        System.out.println(f.getAbsolutePath())//返回此抽象路径名的规范路径名字符串
        System.out.println(f.getPath())//将此抽象路径名转换为一个路径名字符串
        System.out.println(f.hashCode())//计算此抽象路径名的哈希码
        System.out.println(f.length())//返回由此抽象路径名表示的文件的长度
        System.out.println(f.list())// 返回一个字符串数组，这些字符串指定此抽象路径名表示的目录中的文件和目录
        System.out.println(f.mkdir())//创建此抽象路径名指定的目录
    }
}