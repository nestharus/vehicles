I would normally use a proper data test library that unloads data properly and in an automated fashion for testing.

I wrote this sucker because I was tired of manually doing it =P

ie

```
	@BeforeEach
	public void before() {
		educationOrg = data.educationOrg();
		educationProgramNoMatch = data.educationProgram();
		educationProgramMatch = data.educationProgram();

		employerMatch = data.employer();
		jobProfileMatch = data.jobProfile();
		jobPostingMatch = data.jobPosting(jobProfileMatch);
		var employerEducationProgam = new EmployerJobProfileEducationProgram();
		employerEducationProgam.setEducationProgram(educationProgramMatch);
		employerEducationProgam.setFilter(true);
		jobProfileMatch.getJobProfileEducationPrograms().add(employerEducationProgam);

		jobProfileNoMatch = data.jobProfile();
		jobPostingNoMatch = data.jobPosting(jobProfileNoMatch);

		data.save();
	}
	
		@AfterEach
	public void after() {
		data.reset();
	}
```

I also don't like to use RestTemplate when hitting controllers.
Wrote this sucker too, lol. I wanted it to reflect what the RestController looked like so I could follow stuff back. It's too bad I can't use it because the company I worked for owns it :(.

ie
```
        when(() -> put(String.class)
			.withPath(path(EmployerRestController.class)
				.route("setCustomerStatus/{employerId}")
				.parameter("employerId", employer.getId())
				.parameter("flag", "Customer")
				.parameter("value", "true")
			)
			.withUser(User.employer(employer.getId()))
			.execute()
		)
			.thenA(BadException.class)
			.isThrown();
			
        // query param + data example
		final JsonNode state = post(JsonNode.class)
			.withPath(path(JobPostingRestController.class)
				.route("suggest")
				.parameter("limit", "5")
				.parameter("type", type)
			)
			.withUser(student("stanford_alum@stanford.edux"))
			.withData(ConnectMapper.mapper.readValue("{ 'prefix': 'ca' }".replace("'", "\""), Map.class))
			.execute();
```

Also, I know that the elasticsearch lib I talked about was confusing ^)^.
The primary purpose of the module was custom scoring of documents.
The searches that were conducted were complicated and had scoring algorithms. Wasn't as cut and dry as matching some condition. Was matching a condition + how well.

```
    // high level module
    public class CompositeScore extends SearchModule {
        @Module
        private ModuleSort sortModule;
        
        @Module
        private ModuleBehavior1 behaviorModule1;
        
        @Module
        private ModuleBehavior2 behaviorModule2;
        
        @ModuleSetup(SearchArgument.source)
        private void setup() {
            final var source = argument(SearchArgument.source);
            
            setup(source, sortModule);
            setup(source, behaviorModule1);
            setup(source, behaviorModule2);
        }
    }
    
    // low level module
    public class ModuleBehavior1<T extends ModuleBehavior1> extends SearchModule {
        @ModuleName
        public static final String name "behavior1";
        
        private static final String SCORE_SCRIPT_FILENAME = "behavior1_score.java";
        
        // bunch of setters to configure the arguments
        // setters autowired via setup in high level module
        
        @ModulePostProcess
        // can be used for things like aggregation cleanup too
        private void clean() {
            // clear setters
        }
        
        @ModuleSource
        private void query() {
            // the actual elasticsearch query
        }
        
        @ModuleBoost
        private Float boostValue() {
            // custom weighting property
            // value read out of a map that is normally loaded from db via boostType
            return property(boostType);
        }
        
        @ModuleScore
        private ModuleScoreContext score(final Map<String, ObjectNode> hits) {
            // this is for optimization
            // elasticsearch will only give the composite scores
            // the app this was for needed individual scores + composite
            // this computes individual scores for documents after a small set is returned from ES using the same algorithm as plugin
            // the code layout is practically identical to plugin on ES
            // so mostly copy and paste
        }
        
        // these are just a few features of low-level modules
        // there are also things for aggregations (during and post) for example
        
        // we had some crazy modules that created modules within the query and executed them too O_o
    }
    
    // and ofc this thing had a shnazzy API for end use
    // this is a specific use-case that ties to modules
    // this is customized a bit for a test using .module(...)
    // normally would use factory provided modules
    		return jobPostingsToProgramsSearchFactory.builder()
			.jobPosting(job)
			.module(new SearchModule()
				.module(new ModuleMatchAll())
				.module(JobPostingsToProgramsCompositeSearch.createFilter(job))
			)
			.filterById(data.educationProgramIds())
			.search();
```