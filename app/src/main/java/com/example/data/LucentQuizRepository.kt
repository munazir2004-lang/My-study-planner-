package com.example.data

import com.example.model.CurrentAffairsArticle
import com.example.model.LucentSubTopic
import com.example.model.LucentTopic
import com.example.model.QuizQuestion

object LucentQuizRepository {

    // --- Current Affairs Articles with Related Quiz Questions ---
    val currentAffairsArticles: List<CurrentAffairsArticle> = listOf(
        CurrentAffairsArticle(
            id = "ca_1",
            titleHindi = "नालंदा विश्वविद्यालय के नए परिसर का उद्घाटन: ऐतिहासिक धरोहर का पुनरोद्धार",
            titleEnglish = "Inauguration of New Nalanda University Campus: Heritage Revived",
            summaryHindi = "प्राचीन भारत के गौरव नालंदा विश्वविद्यालय के आधुनिक अंतरराष्ट्रीय परिसर का उद्घाटन किया गया, जिसमें 17 देशों की सहभागिता है।",
            summaryEnglish = "The modern international campus of the ancient Nalanda University was inaugurated, featuring diplomatic partnership from 17 countries.",
            category = "Bihar Special",
            date = "1 October 2026",
            readTimeMinutes = 3,
            relatedQuestions = listOf(
                QuizQuestion(
                    id = "ca1_q1",
                    topic = "Current Affairs",
                    subTopic = "Nalanda Heritage",
                    questionHindi = "प्राचीन नालंदा विश्वविद्यालय की स्थापना किस गुप्त शासक ने की थी?",
                    questionEnglish = "Which Gupta ruler founded the ancient Nalanda University?",
                    optionsHindi = listOf("समुद्रगुप्त", "कुमारगुप्त प्रथम", "चंद्रगुप्त द्वितीय", "स्कंदगुप्त"),
                    optionsEnglish = listOf("Samudragupta", "Kumaragupta I", "Chandragupta II", "Skandagupta"),
                    correctAnswerIndex = 1,
                    explanationHindi = "नालंदा विश्वविद्यालय की स्थापना 5वीं शताब्दी ईस्वी में गुप्त सम्राट कुमारगुप्त प्रथम (महेंद्रादित्य) ने की थी।",
                    explanationEnglish = "Ancient Nalanda University was founded in the 5th century CE by Gupta emperor Kumaragupta I."
                ),
                QuizQuestion(
                    id = "ca1_q2",
                    topic = "Current Affairs",
                    subTopic = "Nalanda Heritage",
                    questionHindi = "नालंदा महाविहार को यूनेस्को (UNESCO) विश्व धरोहर स्थल का दर्जा किस वर्ष दिया गया था?",
                    questionEnglish = "In which year was Nalanda Mahavihara inscribed as a UNESCO World Heritage Site?",
                    optionsHindi = listOf("2010", "2014", "2016", "2018"),
                    optionsEnglish = listOf("2010", "2014", "2016", "2018"),
                    correctAnswerIndex = 2,
                    explanationHindi = "नालंदा महाविहार को जुलाई 2016 में यूनेस्को की विश्व धरोहर सूची में शामिल किया गया था।",
                    explanationEnglish = "Nalanda Mahavihara was inscribed on the UNESCO World Heritage list in July 2016."
                )
            )
        ),
        CurrentAffairsArticle(
            id = "ca_2",
            titleHindi = "भारतीय अंतरिक्ष स्टेशन (BAS) मिशन: इसरो का नया रोडमैप जारी",
            titleEnglish = "Bharatiya Antariksha Station (BAS): ISRO Unveils New Roadmap",
            summaryHindi = "इसरो ने 2035 तक स्वतंत्र भारतीय अंतरिक्ष स्टेशन स्थापित करने और गगनयान मिशन के आगामी चरणों की विस्तृत योजना की घोषणा की।",
            summaryEnglish = "ISRO announced its detailed roadmap to establish an independent Bharatiya Antariksha Station by 2035 alongside Gaganyaan stages.",
            category = "Sci-Tech",
            date = "30 September 2026",
            readTimeMinutes = 3,
            relatedQuestions = listOf(
                QuizQuestion(
                    id = "ca2_q1",
                    topic = "Current Affairs",
                    subTopic = "Space Technology",
                    questionHindi = "इसरो (ISRO) की स्थापना किस वर्ष हुई थी?",
                    questionEnglish = "In which year was ISRO established?",
                    optionsHindi = listOf("15 अगस्त 1969", "26 जनवरी 1972", "15 अगस्त 1975", "1 नवंबर 1965"),
                    optionsEnglish = listOf("15 August 1969", "26 January 1972", "15 August 1975", "1 November 1965"),
                    correctAnswerIndex = 0,
                    explanationHindi = "भारतीय अंतरिक्ष अनुसंधान संगठन (ISRO) की स्थापना 15 अगस्त 1969 को डॉ. विक्रम साराभाई के नेतृत्व में हुई थी।",
                    explanationEnglish = "ISRO was established on 15 August 1969 under the leadership of Dr. Vikram Sarabhai."
                ),
                QuizQuestion(
                    id = "ca2_q2",
                    topic = "Current Affairs",
                    subTopic = "Space Technology",
                    questionHindi = "भारत के पहले मानव अंतरिक्ष उड़ान मिशन का नाम क्या है?",
                    questionEnglish = "What is the name of India's first human spaceflight mission?",
                    optionsHindi = listOf("चंद्रयान", "गगनयान", "आदित्य-L1", "मंगलयान"),
                    optionsEnglish = listOf("Chandrayaan", "Gaganyaan", "Aditya-L1", "Mangalyaan"),
                    correctAnswerIndex = 1,
                    explanationHindi = "गगनयान भारत का पहला स्वदेशी मानव अंतरिक्ष उड़ान मिशन है।",
                    explanationEnglish = "Gaganyaan is India's first indigenous human spaceflight mission."
                )
            )
        ),
        CurrentAffairsArticle(
            id = "ca_3",
            titleHindi = "भारतीय अर्थव्यवस्था: 4 ट्रिलियन डॉलर जीडीपी की ओर तीव्र अग्रसर",
            titleEnglish = "Indian Economy: Rapidly Heading Towards 4 Trillion Dollar GDP",
            summaryHindi = "रिजर्व बैंक और सांख्यिकी मंत्रालय के नवीनतम आंकड़ों के अनुसार विनिर्माण एवं सेवा क्षेत्र के मजबूत प्रदर्शन से भारत विश्व की सबसे तेजी से बढ़ती प्रमुख अर्थव्यवस्था बना हुआ है।",
            summaryEnglish = "According to latest statistics, India remains the fastest growing major economy propelled by robust manufacturing and service sectors.",
            category = "Economy",
            date = "29 September 2026",
            readTimeMinutes = 2,
            relatedQuestions = listOf(
                QuizQuestion(
                    id = "ca3_q1",
                    topic = "Current Affairs",
                    subTopic = "Indian Economy",
                    questionHindi = "भारत में मौद्रिक नीति (Monetary Policy) कौन निर्धारित करता है?",
                    questionEnglish = "Who determines the Monetary Policy in India?",
                    optionsHindi = listOf("वित्त मंत्रालय", "भारतीय रिजर्व बैंक (MPC)", "नीति आयोग", "संसद"),
                    optionsEnglish = listOf("Ministry of Finance", "Reserve Bank of India (MPC)", "NITI Aayog", "Parliament"),
                    correctAnswerIndex = 1,
                    explanationHindi = "आरबीआई की मौद्रिक नीति समिति (Monetary Policy Committee - MPC) रेपो दर और मौद्रिक नीति तय करती है।",
                    explanationEnglish = "The Monetary Policy Committee (MPC) of the Reserve Bank of India sets the repo rate and monetary policy."
                )
            )
        ),
        CurrentAffairsArticle(
            id = "ca_4",
            titleHindi = "एशियाई खेलों और ओलंपिक में भारत का ऐतिहासिक प्रदर्शन",
            titleEnglish = "Historic Performance by Indian Contingent in Asian & Olympic Games",
            summaryHindi = "निशानेबाजी, तीरंदाजी, भाला फेंक और बैडमिंटन में भारतीय एथलीटों ने रिकॉर्ड संख्या में पदक जीतकर नया कीर्तिमान स्थापित किया।",
            summaryEnglish = "Indian athletes set a new benchmark by winning record medals across shooting, archery, javelin throw, and badminton.",
            category = "Sports",
            date = "28 September 2026",
            readTimeMinutes = 2,
            relatedQuestions = listOf(
                QuizQuestion(
                    id = "ca4_q1",
                    topic = "Current Affairs",
                    subTopic = "Sports",
                    questionHindi = "भारत के राष्ट्रीय खेल दिवस (29 अगस्त) को किसकी जयंती के रूप में मनाया जाता है?",
                    questionEnglish = "National Sports Day of India (29 August) is celebrated to honor whose birth anniversary?",
                    optionsHindi = listOf("मेजर ध्यानचंद", "मिल्खा सिंह", "के डी जाधव", "सीके नायडू"),
                    optionsEnglish = listOf("Major Dhyan Chand", "Milkha Singh", "K.D. Jadhav", "C.K. Nayudu"),
                    correctAnswerIndex = 0,
                    explanationHindi = "हॉकी के जादूगर मेजर ध्यानचंद की जयंती के उपलक्ष्य में हर वर्ष 29 अगस्त को राष्ट्रीय खेल दिवस मनाया जाता है।",
                    explanationEnglish = "National Sports Day is celebrated on August 29 every year in memory of hockey wizard Major Dhyan Chand."
                )
            )
        )
    )

    // --- Lucent GK Topic-Wise Question Bank ---
    val lucentTopics: List<LucentTopic> = listOf(
        LucentTopic(
            id = "hist",
            nameHindi = "भारतीय इतिहास (History)",
            nameEnglish = "Indian History",
            iconName = "history",
            colorHex = "#EF4444",
            subTopics = listOf(
                LucentSubTopic(
                    id = "hist_ancient",
                    titleHindi = "प्राचीन भारत (सिंधु सभ्यता, वैदिक व मौर्य काल)",
                    titleEnglish = "Ancient India (Indus, Vedic & Maurya)",
                    questionCount = 5,
                    questions = listOf(
                        QuizQuestion(
                            id = "ha_1",
                            topic = "Ancient History",
                            subTopic = "Indus Valley",
                            questionHindi = "सिंधु घाटी सभ्यता का प्रमुख बंदरगाह नगर कौन सा था?",
                            questionEnglish = "Which was the major port city of the Indus Valley Civilization?",
                            optionsHindi = listOf("हड़प्पा", "लोथल", "कालीबंगा", "मोहनजोदड़ो"),
                            optionsEnglish = listOf("Harappa", "Lothal", "Kalibangan", "Mohenjo-daro"),
                            correctAnswerIndex = 1,
                            explanationHindi = "लोथल (गुजरात में भोगवा नदी के तट पर) सिंधु घाटी सभ्यता का प्रमुख बंदरगाह (गोदीबाड़ा) था।",
                            explanationEnglish = "Lothal (on the banks of Bhogava river in Gujarat) was the prominent port/dockyard of the Indus Valley Civilization."
                        ),
                        QuizQuestion(
                            id = "ha_2",
                            topic = "Ancient History",
                            subTopic = "Vedic Age",
                            questionHindi = "'सत्यमेव जयते' शब्द किस उपनिषद से लिया गया है?",
                            questionEnglish = "The phrase 'Satyameva Jayate' has been taken from which Upanishad?",
                            optionsHindi = listOf("कठोपनिषद", "मुण्डकोपनिषद", "छांदोग्य उपनिषद", "केनोपनिषद"),
                            optionsEnglish = listOf("Katha Upanishad", "Mundaka Upanishad", "Chandogya Upanishad", "Kena Upanishad"),
                            correctAnswerIndex = 1,
                            explanationHindi = "भारत का राष्ट्रीय आदर्श वाक्य 'सत्यमेव जयते' मुण्डकोपनिषद से उद्धृत किया गया है।",
                            explanationEnglish = "India's national motto 'Satyameva Jayate' (Truth alone triumphs) is taken from Mundaka Upanishad."
                        ),
                        QuizQuestion(
                            id = "ha_3",
                            topic = "Ancient History",
                            subTopic = "Buddhism",
                            questionHindi = "भगवान बुद्ध ने अपना पहला उपदेश (धर्मचक्रप्रवर्तन) कहाँ दिया था?",
                            questionEnglish = "Where did Lord Buddha deliver his first sermon (Dharmachakrapravartana)?",
                            optionsHindi = listOf("बोधगया", "सारनाथ", "कुशीनगर", "लुंबिनी"),
                            optionsEnglish = listOf("Bodh Gaya", "Sarnath", "Kushinagar", "Lumbini"),
                            correctAnswerIndex = 1,
                            explanationHindi = "भगवान बुद्ध ने ज्ञान प्राप्ति के पश्चात सारनाथ (ऋषिपत्तन) में अपने पांच शिष्यों को प्रथम उपदेश दिया था।",
                            explanationEnglish = "Lord Buddha gave his first sermon to five disciples at Sarnath (Rishipattana) near Varanasi."
                        ),
                        QuizQuestion(
                            id = "ha_4",
                            topic = "Ancient History",
                            subTopic = "Mauryan Empire",
                            questionHindi = "मेगस्थनीज किसके शासनकाल में भारत आया था?",
                            questionEnglish = "Megasthenes visited India during the reign of which ruler?",
                            optionsHindi = listOf("चंद्रगुप्त मौर्य", "बिंदुसार", "अशोक", "हर्षवर्धन"),
                            optionsEnglish = listOf("Chandragupta Maurya", "Bindusara", "Ashoka", "Harshavardhana"),
                            correctAnswerIndex = 0,
                            explanationHindi = "मेगस्थनीज सेल्यूकस निकेटर का राजदूत था, जो चंद्रगुप्त मौर्य के दरबार में आया और उसने 'इंडिका' पुस्तक लिखी।",
                            explanationEnglish = "Megasthenes was an ambassador of Seleucus Nicator who visited the court of Chandragupta Maurya and wrote 'Indica'."
                        ),
                        QuizQuestion(
                            id = "ha_5",
                            topic = "Ancient History",
                            subTopic = "Gupta Empire",
                            questionHindi = "किस गुप्त शासक को 'भारत का नेपोलियन' कहा जाता है?",
                            questionEnglish = "Which Gupta ruler is known as the 'Napoleon of India'?",
                            optionsHindi = listOf("चंद्रगुप्त प्रथम", "समुद्रगुप्त", "चंद्रगुप्त द्वितीय (विक्रमादित्य)", "कुमारगुप्त"),
                            optionsEnglish = listOf("Chandragupta I", "Samudragupta", "Chandragupta II", "Kumaragupta"),
                            correctAnswerIndex = 1,
                            explanationHindi = "इतिहासकार विंसेंट स्मिथ ने समुद्रगुप्त के सैन्य विजय अभियानों के कारण उसे 'भारत का नेपोलियन' कहा था।",
                            explanationEnglish = "Historian V.A. Smith called Samudragupta the 'Napoleon of India' due to his military conquests."
                        )
                    )
                ),
                LucentSubTopic(
                    id = "hist_medieval",
                    titleHindi = "मध्यकालीन भारत (दिल्ली सल्तनत व मुगल काल)",
                    titleEnglish = "Medieval India (Sultanate & Mughals)",
                    questionCount = 4,
                    questions = listOf(
                        QuizQuestion(
                            id = "hm_1",
                            topic = "Medieval History",
                            subTopic = "Delhi Sultanate",
                            questionHindi = "आगरा शहर की स्थापना किस शासक ने की थी?",
                            questionEnglish = "Which ruler founded the city of Agra?",
                            optionsHindi = listOf("बहलोल लोदी", "सिकंदर लोदी", "इब्राहिम लोदी", "अलाउद्दीन खिलजी"),
                            optionsEnglish = listOf("Bahlul Lodi", "Sikandar Lodi", "Ibrahim Lodi", "Alauddin Khalji"),
                            correctAnswerIndex = 1,
                            explanationHindi = "सिकंदर लोदी ने 1504 ईस्वी में आगरा शहर की नींव रखी और उसे अपनी राजधानी बनाया।",
                            explanationEnglish = "Sikandar Lodi founded the city of Agra in 1504 CE and made it his capital."
                        ),
                        QuizQuestion(
                            id = "hm_2",
                            topic = "Medieval History",
                            subTopic = "Mughal Empire",
                            questionHindi = "पानीपत का प्रथम युद्ध (1526 ई.) किसके बीच लड़ा गया था?",
                            questionEnglish = "The First Battle of Panipat (1526 CE) was fought between whom?",
                            optionsHindi = listOf("बाबर और इब्राहिम लोदी", "अकबर और हेमू", "बाबर और राणा सांगा", "हुमायूँ और शेरशाह सूरी"),
                            optionsEnglish = listOf("Babur and Ibrahim Lodi", "Akbar and Hemu", "Babur and Rana Sanga", "Humayun and Sher Shah Suri"),
                            correctAnswerIndex = 0,
                            explanationHindi = "21 अप्रैल 1526 को पानीपत के पहले युद्ध में बाबर ने इब्राहिम लोदी को हराकर भारत में मुगल साम्राज्य की नींव रखी।",
                            explanationEnglish = "On 21 April 1526, Babur defeated Ibrahim Lodi establishing the Mughal Empire in India."
                        ),
                        QuizQuestion(
                            id = "hm_3",
                            topic = "Medieval History",
                            subTopic = "Mughal Empire",
                            questionHindi = "'दीन-ए-इलाही' धर्म की शुरुआत किस मुगल बादशाह ने की थी?",
                            questionEnglish = "Which Mughal emperor introduced the 'Din-i-Ilahi' faith?",
                            optionsHindi = listOf("बाबर", "अकबर", "जहाँगीर", "शाहजहाँ"),
                            optionsEnglish = listOf("Babur", "Akbar", "Jahangir", "Shah Jahan"),
                            correctAnswerIndex = 1,
                            explanationHindi = "अकबर ने 1582 ईस्वी में सभी धर्मों के मूल सिद्धांतों को मिलाकर 'दीन-ए-इलाही' (तौहीद-ए-इलाही) की शुरुआत की थी। बीरबल इसे स्वीकार करने वाला एकमात्र हिंदू था।",
                            explanationEnglish = "Akbar introduced Din-i-Ilahi in 1582 CE blending elements of various religions. Birbal was the only Hindu to accept it."
                        ),
                        QuizQuestion(
                            id = "hm_4",
                            topic = "Medieval History",
                            subTopic = "Sher Shah Suri",
                            questionHindi = "ग्रैंड ट्रंक रोड (GT Road) का निर्माण किस शासक ने करवाया था?",
                            questionEnglish = "Grand Trunk Road (GT Road) was reconstructed by which ruler?",
                            optionsHindi = listOf("शेरशाह सूरी", "अकबर", "अलाउद्दीन खिलजी", "फिरोज शाह तुगलक"),
                            optionsEnglish = listOf("Sher Shah Suri", "Akbar", "Alauddin Khalji", "Firoz Shah Tughlaq"),
                            correctAnswerIndex = 0,
                            explanationHindi = "शेरशाह सूरी ने बंगाल के सोनारगांव से पेशावर तक प्रसिद्ध 'सड़क-ए-आजम' (GT Road) का निर्माण कराया था।",
                            explanationEnglish = "Sher Shah Suri constructed the famous Sadak-e-Azam (Grand Trunk Road) connecting Sonargaon in Bengal to Peshawar."
                        )
                    )
                ),
                LucentSubTopic(
                    id = "hist_modern",
                    titleHindi = "आधुनिक भारत व स्वतंत्रता संग्राम (1857-1947)",
                    titleEnglish = "Modern India & Freedom Movement",
                    questionCount = 4,
                    questions = listOf(
                        QuizQuestion(
                            id = "hmod_1",
                            topic = "Modern History",
                            subTopic = "1857 Revolt",
                            questionHindi = "1857 के विद्रोह के समय भारत का गवर्नर जनरल कौन था?",
                            questionEnglish = "Who was the Governor-General of India during the Revolt of 1857?",
                            optionsHindi = listOf("लॉर्ड डलहौजी", "लॉर्ड कैनिंग", "लॉर्ड कर्जन", "लॉर्ड माउंटबेटन"),
                            optionsEnglish = listOf("Lord Dalhousie", "Lord Canning", "Lord Curzon", "Lord Mountbatten"),
                            correctAnswerIndex = 1,
                            explanationHindi = "1857 के सिपाही विद्रोह के समय लॉर्ड कैनिंग भारत का गवर्नर जनरल था, जो बाद में 1858 में भारत का पहला वायसराय बना।",
                            explanationEnglish = "Lord Canning was the Governor-General during the 1857 revolt, later becoming India's first Viceroy in 1858."
                        ),
                        QuizQuestion(
                            id = "hmod_2",
                            topic = "Modern History",
                            subTopic = "Indian National Congress",
                            questionHindi = "भारतीय राष्ट्रीय कांग्रेस (INC) की स्थापना 1885 में किसके द्वारा की गई थी?",
                            questionEnglish = "By whom was the Indian National Congress (INC) founded in 1885?",
                            optionsHindi = listOf("ए.ओ. ह्यूम", "व्योमेश चंद्र बनर्जी", "दादाभाई नौरोजी", "गोपाल कृष्ण गोखले"),
                            optionsEnglish = listOf("A.O. Hume", "W.C. Bonnerjee", "Dadabhai Naoroji", "Gopal Krishna Gokhale"),
                            correctAnswerIndex = 0,
                            explanationHindi = "कांग्रेस की स्थापना 28 दिसंबर 1885 को बॉम्बे में एलेन ऑक्टेवियन ह्यूम (A.O. Hume) द्वारा की गई थी। इसके प्रथम अध्यक्ष व्योमेश चंद्र बनर्जी थे।",
                            explanationEnglish = "INC was founded on 28 Dec 1885 in Bombay by retired civil servant A.O. Hume. W.C. Bonnerjee was its first president."
                        ),
                        QuizQuestion(
                            id = "hmod_3",
                            topic = "Modern History",
                            subTopic = "Gandhian Era",
                            questionHindi = "महात्मा गांधी ने भारत में अपना पहला सत्याग्रह 1917 में कहाँ शुरू किया था?",
                            questionEnglish = "Where did Mahatma Gandhi launch his first Satyagraha in India in 1917?",
                            optionsHindi = listOf("अहमदाबाद", "खेड़ा", "चंपारण (बिहार)", "दांडी"),
                            optionsEnglish = listOf("Ahmedabad", "Kheda", "Champaran (Bihar)", "Dandi"),
                            correctAnswerIndex = 2,
                            explanationHindi = "गांधीजी ने नील की तिनकठिया प्रथा के विरोध में राजकुमार शुक्ल के आमंत्रण पर बिहार के चंपारण में 1917 में पहला सफल सत्याग्रह किया।",
                            explanationEnglish = "Gandhi led his first successful Satyagraha in Champaran, Bihar in 1917 against the Tinkathia system of indigo cultivation."
                        ),
                        QuizQuestion(
                            id = "hmod_4",
                            topic = "Modern History",
                            subTopic = "Freedom Movement",
                            questionHindi = "'करो या मरो' (Do or Die) का नारा गांधीजी ने किस आंदोलन में दिया था?",
                            questionEnglish = "In which movement did Gandhiji give the slogan 'Do or Die'?",
                            optionsHindi = listOf("असहयोग आंदोलन (1920)", "सविनय अवज्ञा आंदोलन (1930)", "भारत छोड़ो आंदोलन (1942)", "खिलाफत आंदोलन (1919)"),
                            optionsEnglish = listOf("Non-Cooperation Movement", "Civil Disobedience Movement", "Quit India Movement (1942)", "Khilafat Movement"),
                            correctAnswerIndex = 2,
                            explanationHindi = "8 अगस्त 1942 को बॉम्बे के ग्वालिया टैंक मैदान से भारत छोड़ो आंदोलन का आह्वान करते हुए गांधीजी ने 'करो या मरो' का नारा दिया था।",
                            explanationEnglish = "Gandhiji gave the clarion call 'Do or Die' on 8 August 1942 during the launch of the Quit India Movement."
                        )
                    )
                )
            )
        ),
        LucentTopic(
            id = "polity",
            nameHindi = "भारतीय राजव्यवस्था (Indian Polity)",
            nameEnglish = "Indian Polity & Constitution",
            iconName = "gavel",
            colorHex = "#3B82F6",
            subTopics = listOf(
                LucentSubTopic(
                    id = "pol_constitution",
                    titleHindi = "संविधान सभा व मौलिक अधिकार",
                    titleEnglish = "Constituent Assembly & Fundamental Rights",
                    questionCount = 4,
                    questions = listOf(
                        QuizQuestion(
                            id = "pol_1",
                            topic = "Indian Polity",
                            subTopic = "Constituent Assembly",
                            questionHindi = "संविधान सभा की प्रारूप समिति (Drafting Committee) के अध्यक्ष कौन थे?",
                            questionEnglish = "Who was the Chairman of the Drafting Committee of the Constituent Assembly?",
                            optionsHindi = listOf("डॉ. राजेंद्र प्रसाद", "डॉ. बी.आर. अंबेडकर", "जवाहरलाल नेहरू", "बी.एन. राव"),
                            optionsEnglish = listOf("Dr. Rajendra Prasad", "Dr. B.R. Ambedkar", "Jawaharlal Nehru", "B.N. Rau"),
                            correctAnswerIndex = 1,
                            explanationHindi = "डॉ. भीमराव अंबेडकर 29 अगस्त 1947 को गठित 7 सदस्यीय प्रारूप समिति के अध्यक्ष थे।",
                            explanationEnglish = "Dr. B.R. Ambedkar was the Chairman of the 7-member Drafting Committee appointed on 29 August 1947."
                        ),
                        QuizQuestion(
                            id = "pol_2",
                            topic = "Indian Polity",
                            subTopic = "Fundamental Rights",
                            questionHindi = "डॉ. अंबेडकर ने किस अनुच्छेद को 'संविधान का हृदय और आत्मा' कहा था?",
                            questionEnglish = "Which Article did Dr. Ambedkar call the 'Heart and Soul of the Constitution'?",
                            optionsHindi = listOf("अनुच्छेद 14", "अनुच्छेद 19", "अनुच्छेद 21", "अनुच्छेद 32"),
                            optionsEnglish = listOf("Article 14", "Article 19", "Article 21", "Article 32"),
                            correctAnswerIndex = 3,
                            explanationHindi = "अनुच्छेद 32 (संवैधानिक उपचारों का अधिकार) को डॉ. अंबेडकर ने संविधान की आत्मा और हृदय कहा था, क्योंकि इसके तहत सुप्रीम कोर्ट रिट जारी करता है।",
                            explanationEnglish = "Article 32 (Right to Constitutional Remedies) was termed the 'Heart and Soul' of the Constitution by Dr. Ambedkar."
                        ),
                        QuizQuestion(
                            id = "pol_3",
                            topic = "Indian Polity",
                            subTopic = "Preamble",
                            questionHindi = "42वें संविधान संशोधन (1976) द्वारा प्रस्तावना में कौन से शब्द जोड़े गए?",
                            questionEnglish = "Which words were added to the Preamble by the 42nd Amendment Act (1976)?",
                            optionsHindi = listOf("समाजवादी, पंथनिरपेक्ष और अखंडता", "संप्रभु, लोकतांत्रिक और गणराज्य", "स्वतंत्रता, समता और बंधुत्व", "न्याय, विचार और अभिव्यक्ति"),
                            optionsEnglish = listOf("Socialist, Secular, Integrity", "Sovereign, Democratic, Republic", "Liberty, Equality, Fraternity", "Justice, Liberty, Dignity"),
                            correctAnswerIndex = 0,
                            explanationHindi = "1976 के 42वें संशोधन द्वारा प्रस्तावना में 'समाजवादी' (Socialist), 'पंथनिरपेक्ष' (Secular) और 'अखंडता' (Integrity) शब्द जोड़े गए।",
                            explanationEnglish = "The words 'Socialist', 'Secular', and 'Integrity' were added to the Preamble by the 42nd Constitutional Amendment in 1976."
                        ),
                        QuizQuestion(
                            id = "pol_4",
                            topic = "Indian Polity",
                            subTopic = "Fundamental Duties",
                            questionHindi = "मौलिक कर्तव्य (Fundamental Duties) किस देश के संविधान से लिए गए हैं?",
                            questionEnglish = "Fundamental Duties in the Indian Constitution were adopted from which country?",
                            optionsHindi = listOf("संयुक्त राज्य अमेरिका", "पूर्व सोवियत संघ (USSR/रूस)", "ब्रिटेन", "आयरलैंड"),
                            optionsEnglish = listOf("USA", "Former USSR (Russia)", "United Kingdom", "Ireland"),
                            correctAnswerIndex = 1,
                            explanationHindi = "स्वर्ण सिंह समिति की सिफारिश पर 42वें संशोधन द्वारा भाग IV-A (अनुच्छेद 51A) में मौलिक कर्तव्य रूस (USSR) के संविधान से प्रेरित होकर जोड़े गए।",
                            explanationEnglish = "Fundamental Duties (Part IV-A, Article 51A) were borrowed from the Constitution of the USSR upon recommendation of the Swaran Singh Committee."
                        )
                    )
                ),
                LucentSubTopic(
                    id = "pol_parliament",
                    titleHindi = "राष्ट्रपति, संसद एवं न्यायपालिका",
                    titleEnglish = "President, Parliament & Judiciary",
                    questionCount = 3,
                    questions = listOf(
                        QuizQuestion(
                            id = "pol_p1",
                            topic = "Indian Polity",
                            subTopic = "President of India",
                            questionHindi = "भारतीय संविधान के अनुसार देश का प्रथम नागरिक कौन होता है?",
                            questionEnglish = "According to the Indian Constitution, who is the first citizen of India?",
                            optionsHindi = listOf("प्रधानमंत्री", "भारत का राष्ट्रपति", "मुख्य न्यायाधीश", "लोकसभा अध्यक्ष"),
                            optionsEnglish = listOf("Prime Minister", "President of India", "Chief Justice of India", "Speaker of Lok Sabha"),
                            correctAnswerIndex = 1,
                            explanationHindi = "भारत का राष्ट्रपति राज्य का प्रमुख और भारत का प्रथम नागरिक होता है (अनुच्छेद 52)।",
                            explanationEnglish = "The President of India is the head of state and the first citizen of India (Article 52)."
                        ),
                        QuizQuestion(
                            id = "pol_p2",
                            topic = "Indian Polity",
                            subTopic = "Parliament",
                            questionHindi = "राज्यसभा का पदेन सभापति (Ex-officio Chairman) कौन होता है?",
                            questionEnglish = "Who is the Ex-officio Chairman of Rajya Sabha?",
                            optionsHindi = listOf("राष्ट्रपति", "उपराष्ट्रपति", "प्रधानमंत्री", "गृह मंत्री"),
                            optionsEnglish = listOf("President", "Vice-President", "Prime Minister", "Home Minister"),
                            correctAnswerIndex = 1,
                            explanationHindi = "अनुच्छेद 64 के अनुसार भारत का उपराष्ट्रपति राज्यसभा का पदेन सभापति होता है।",
                            explanationEnglish = "Under Article 64, the Vice-President of India is the ex-officio Chairman of the Rajya Sabha."
                        ),
                        QuizQuestion(
                            id = "pol_p3",
                            topic = "Indian Polity",
                            subTopic = "Judiciary",
                            questionHindi = "भारत के सर्वोच्च न्यायालय के न्यायाधीशों की सेवानिवृत्ति आयु कितनी है?",
                            questionEnglish = "What is the retirement age of Supreme Court judges in India?",
                            optionsHindi = listOf("60 वर्ष", "62 वर्ष", "65 वर्ष", "68 वर्ष"),
                            optionsEnglish = listOf("60 years", "62 years", "65 years", "68 years"),
                            correctAnswerIndex = 2,
                            explanationHindi = "सुप्रीम कोर्ट के न्यायाधीश 65 वर्ष की आयु में सेवानिवृत्त होते हैं, जबकि उच्च न्यायालय (High Court) के न्यायाधीश 62 वर्ष में।",
                            explanationEnglish = "Supreme Court judges retire at 65 years, whereas High Court judges retire at 62 years of age."
                        )
                    )
                )
            )
        ),
        LucentTopic(
            id = "geo",
            nameHindi = "भूगोल (Geography)",
            nameEnglish = "Geography",
            iconName = "public",
            colorHex = "#10B981",
            subTopics = listOf(
                LucentSubTopic(
                    id = "geo_india",
                    titleHindi = "भारत का भूगोल (नदियाँ, पर्वत, दर्रे)",
                    titleEnglish = "Indian Geography (Rivers, Mountains, Passes)",
                    questionCount = 4,
                    questions = listOf(
                        QuizQuestion(
                            id = "geo_1",
                            topic = "Geography",
                            subTopic = "Rivers of India",
                            questionHindi = "दक्षिण भारत की गंगा (Ganga of South India) किसे कहा जाता है?",
                            questionEnglish = "Which river is known as the 'Ganga of the South'?",
                            optionsHindi = listOf("गोदावरी", "कृष्णा", "कावेरी", "महानदी"),
                            optionsEnglish = listOf("Godavari", "Krishna", "Cauvery", "Mahanadi"),
                            correctAnswerIndex = 0,
                            explanationHindi = "गोदावरी को प्रायद्वीपीय भारत की सबसे लंबी नदी होने के कारण 'दक्षिण गंगा' या 'वृद्ध गंगा' कहा जाता है।",
                            explanationEnglish = "Godavari is known as Dakshin Ganga or Vriddha Ganga as it is the longest peninsular river."
                        ),
                        QuizQuestion(
                            id = "geo_2",
                            topic = "Geography",
                            subTopic = "Physical Features",
                            questionHindi = "भारत की सबसे ऊँची पर्वत चोटी कौन सी है (POK सहित)?",
                            questionEnglish = "Which is the highest mountain peak of India?",
                            optionsHindi = listOf("कंचनजंघा", "गॉडविन ऑस्टिन (K2)", "नंदा देवी", "कामेत"),
                            optionsEnglish = listOf("Kanchenjunga", "Godwin Austen (K2)", "Nanda Devi", "Kamet"),
                            correctAnswerIndex = 1,
                            explanationHindi = "K2 (गॉडविन ऑस्टिन, 8,611 मी.) भारत की सबसे ऊँची चोटी है। निर्विवाद भारतीय भूभाग में कंचनजंघा (8,586 मी.) सिक्किम में स्थित है।",
                            explanationEnglish = "K2 (8,611m) in the Karakoram range is the highest peak in India, while Kanchenjunga (8,586m) in Sikkim is the highest peak entirely in undisputed territory."
                        ),
                        QuizQuestion(
                            id = "geo_3",
                            topic = "Geography",
                            subTopic = "Solar System",
                            questionHindi = "सौरमंडल का सबसे चमकीला और गर्म ग्रह कौन सा है?",
                            questionEnglish = "Which is the brightest and hottest planet in our solar system?",
                            optionsHindi = listOf("बुध (Mercury)", "शुक्र (Venus)", "मंगल (Mars)", "बृहस्पति (Jupiter)"),
                            optionsEnglish = listOf("Mercury", "Venus", "Mars", "Jupiter"),
                            correctAnswerIndex = 1,
                            explanationHindi = "शुक्र (Venus) को 'भोर का तारा' व 'सांझ का तारा' भी कहते हैं। सघन कार्बन डाइऑक्साइड वातावरण के कारण यह सबसे गर्म ग्रह है।",
                            explanationEnglish = "Venus is the hottest planet due to greenhouse effect of CO2 and is known as the Morning & Evening Star."
                        ),
                        QuizQuestion(
                            id = "geo_4",
                            topic = "Geography",
                            subTopic = "Climate & Soil",
                            questionHindi = "कपास की खेती के लिए सर्वाधिक उपयुक्त मिट्टी कौन सी है?",
                            questionEnglish = "Which soil is best suited for cotton cultivation?",
                            optionsHindi = listOf("जलोढ़ मिट्टी", "काली मिट्टी (रेगुर)", "लाल मिट्टी", "लैटेराइट मिट्टी"),
                            optionsEnglish = listOf("Alluvial Soil", "Black Soil (Regur)", "Red Soil", "Laterite Soil"),
                            correctAnswerIndex = 1,
                            explanationHindi = "काली मिट्टी को रेगुर मिट्टी या 'कपासी मिट्टी' कहा जाता है। इसमें जल धारण क्षमता सर्वाधिक होती है।",
                            explanationEnglish = "Black soil, also known as Regur or Black Cotton Soil, has high water retention capacity suitable for cotton."
                        )
                    )
                )
            )
        ),
        LucentTopic(
            id = "sci",
            nameHindi = "सामान्य विज्ञान (General Science)",
            nameEnglish = "General Science",
            iconName = "biotech",
            colorHex = "#8B5CF6",
            subTopics = listOf(
                LucentSubTopic(
                    id = "sci_bio",
                    titleHindi = "जीव विज्ञान व मानव शरीर (Biology & Nutrition)",
                    titleEnglish = "Biology, Human Body & Diseases",
                    questionCount = 3,
                    questions = listOf(
                        QuizQuestion(
                            id = "sci_b1",
                            topic = "General Science",
                            subTopic = "Biology",
                            questionHindi = "मानव शरीर की सबसे बड़ी ग्रंथि (Largest Gland) कौन सी है?",
                            questionEnglish = "Which is the largest gland in the human body?",
                            optionsHindi = listOf("अग्न्याशय (Pancreas)", "यकृत (Liver)", "थायराइड (Thyroid)", "पीयूष ग्रंथि (Pituitary)"),
                            optionsEnglish = listOf("Pancreas", "Liver", "Thyroid", "Pituitary"),
                            correctAnswerIndex = 1,
                            explanationHindi = "यकृत (Liver) मानव शरीर की सबसे बड़ी ग्रंथि है, जो पित्त रस (Bile juice) का स्राव करती है।",
                            explanationEnglish = "The Liver is the largest gland in the human body, responsible for bile production and metabolism."
                        ),
                        QuizQuestion(
                            id = "sci_b2",
                            topic = "General Science",
                            subTopic = "Vitamins",
                            questionHindi = "रिकेट्स (Rickets) रोग किस विटामिन की कमी से होता है?",
                            questionEnglish = "Rickets is caused by the deficiency of which vitamin?",
                            optionsHindi = listOf("विटामिन A", "विटामिन B", "विटामिन C", "विटामिन D"),
                            optionsEnglish = listOf("Vitamin A", "Vitamin B", "Vitamin C", "Vitamin D"),
                            correctAnswerIndex = 3,
                            explanationHindi = "विटामिन D (कैल्सीफेरोल) की कमी से बच्चों में रिकेट्स (सूखा रोग) तथा वयस्कों में ऑस्टियोमलेशिया होता है।",
                            explanationEnglish = "Deficiency of Vitamin D causes Rickets in children and Osteomalacia in adults."
                        ),
                        QuizQuestion(
                            id = "sci_b3",
                            topic = "General Science",
                            subTopic = "Physics",
                            questionHindi = "ध्वनि की गति किस माध्यम में सर्वाधिक होती है?",
                            questionEnglish = "Speed of sound is maximum in which medium?",
                            optionsHindi = listOf("ठोस (Solid)", "द्रव (Liquid)", "गैस (Gas)", "निर्वात (Vacuum)"),
                            optionsEnglish = listOf("Solid", "Liquid", "Gas", "Vacuum"),
                            correctAnswerIndex = 0,
                            explanationHindi = "ध्वनि अनुदैर्ध्य तरंग है। इसकी गति ठोस (जैसे स्टील) में सर्वाधिक होती है, और यह निर्वात में गमन नहीं कर सकती।",
                            explanationEnglish = "Sound travels fastest in solids (e.g. steel) and cannot propagate in a vacuum."
                        )
                    )
                )
            )
        ),
        LucentTopic(
            id = "bihar",
            nameHindi = "बिहार सामान्य ज्ञान (BPSC Special)",
            nameEnglish = "Bihar Special GK",
            iconName = "school",
            colorHex = "#F59E0B",
            subTopics = listOf(
                LucentSubTopic(
                    id = "bihar_gk",
                    titleHindi = "बिहार का इतिहास एवं भूगोल",
                    titleEnglish = "Bihar History, Polity & Geography",
                    questionCount = 3,
                    questions = listOf(
                        QuizQuestion(
                            id = "bih_1",
                            topic = "Bihar GK",
                            subTopic = "Ancient Bihar",
                            questionHindi = "प्राचीन मगध साम्राज्य की प्रारंभिक राजधानी कौन सी थी?",
                            questionEnglish = "What was the initial capital of the ancient Magadha Kingdom?",
                            optionsHindi = listOf("पाटलिपुत्र", "राजगृह (गिरिव्रज)", "वैशाली", "चंपा"),
                            optionsEnglish = listOf("Pataliputra", "Rajagriha (Girivraja)", "Vaishali", "Champa"),
                            correctAnswerIndex = 1,
                            explanationHindi = "मगध की पहली राजधानी राजगृह (गिरिव्रज) थी, जिसे बिंबिसार ने बसाया था। बाद में उदयन ने पाटलिपुत्र को राजधानी बनाया।",
                            explanationEnglish = "The initial capital of Magadha was Rajagriha (Girivraja). Later, Udayin shifted it to Pataliputra."
                        ),
                        QuizQuestion(
                            id = "bih_2",
                            topic = "Bihar GK",
                            subTopic = "Freedom Struggle",
                            questionHindi = "1857 की क्रांति में बिहार के जगदीशपुर से नेतृत्व किसने किया था?",
                            questionEnglish = "Who led the Revolt of 1857 from Jagdishpur in Bihar?",
                            optionsHindi = listOf("कुंवर सिंह", "अमर सिंह", "पीर अली", "मौलवी अहमदुल्लाह"),
                            optionsEnglish = listOf("Kunwar Singh", "Amar Singh", "Peer Ali", "Maulvi Ahmadullah"),
                            correctAnswerIndex = 0,
                            explanationHindi = "80 वर्षीय वीर कुंवर सिंह ने जगदीशपुर (भोजपुर) से 1857 के विद्रोह का ऐतिहासिक नेतृत्व किया और अंग्रेजों को कई बार पराजित किया।",
                            explanationEnglish = "Veer Kunwar Singh of Jagdishpur (Bhojpur) led the 1857 uprising in Bihar with valor against British forces."
                        ),
                        QuizQuestion(
                            id = "bih_3",
                            topic = "Bihar GK",
                            subTopic = "Geography",
                            questionHindi = "बिहार का शोक (Sorrow of Bihar) किस नदी को कहा जाता है?",
                            questionEnglish = "Which river is known as the 'Sorrow of Bihar'?",
                            optionsHindi = listOf("गंगा", "कोसी", "गंडक", "सोन"),
                            optionsEnglish = listOf("Ganga", "Kosi", "Gandak", "Son"),
                            correctAnswerIndex = 1,
                            explanationHindi = "कोसी नदी अपने निरंतर मार्ग परिवर्तन और विनाशकारी बाढ़ के कारण 'बिहार का शोक' कहलाती है।",
                            explanationEnglish = "Kosi river is called the 'Sorrow of Bihar' due to its frequent course changes and devastating floods."
                        )
                    )
                )
            )
        )
    )
}
