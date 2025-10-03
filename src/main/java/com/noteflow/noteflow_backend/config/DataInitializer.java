package com.noteflow.noteflow_backend.config;

import com.noteflow.noteflow_backend.model.User;
import com.noteflow.noteflow_backend.model.Category;
import com.noteflow.noteflow_backend.model.Folder;
import com.noteflow.noteflow_backend.model.Note;
import com.noteflow.noteflow_backend.repository.UserRepository;
import com.noteflow.noteflow_backend.repository.CategoryRepository;
import com.noteflow.noteflow_backend.repository.FolderRepository;
import com.noteflow.noteflow_backend.repository.NoteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            FolderRepository folderRepository,
            NoteRepository noteRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            // Check if data already exists
            if (userRepository.count() > 0) {
                System.out.println("Database already initialized, skipping...");
                return;
            }

            // ========== Create Users ==========
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@noteflow.com");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setFullName("Admin User");
            adminUser = userRepository.save(adminUser);

            User demoUser = new User();
            demoUser.setUsername("demo");
            demoUser.setEmail("demo@noteflow.com");
            demoUser.setPassword(passwordEncoder.encode("demo123"));
            demoUser.setFullName("Demo User");
            demoUser = userRepository.save(demoUser);

            User johnDoe = new User();
            johnDoe.setUsername("johndoe");
            johnDoe.setEmail("john.doe@example.com");
            johnDoe.setPassword(passwordEncoder.encode("password123"));
            johnDoe.setFullName("John Doe");
            johnDoe = userRepository.save(johnDoe);

            // ========== Create Categories for Demo User ==========
            Category workCategory = new Category();
            workCategory.setName("Work");
            workCategory.setUser(demoUser);
            workCategory = categoryRepository.save(workCategory);

            Category personalCategory = new Category();
            personalCategory.setName("Personal");
            personalCategory.setUser(demoUser);
            personalCategory = categoryRepository.save(personalCategory);

            Category studyCategory = new Category();
            studyCategory.setName("Study");
            studyCategory.setUser(demoUser);
            studyCategory = categoryRepository.save(studyCategory);

            // ========== Create Categories for John Doe ==========
            Category projectsCategory = new Category();
            projectsCategory.setName("Projects");
            projectsCategory.setUser(johnDoe);
            projectsCategory = categoryRepository.save(projectsCategory);

            Category ideasCategory = new Category();
            ideasCategory.setName("Ideas");
            ideasCategory.setUser(johnDoe);
            ideasCategory = categoryRepository.save(ideasCategory);

            // ========== Create Folders for Demo User ==========
            // Work Category Folders
            Folder meetingsFolder = new Folder();
            meetingsFolder.setName("Meetings");
            meetingsFolder.setDescription("All meeting notes and agendas");
            meetingsFolder.setCategory(workCategory);
            meetingsFolder = folderRepository.save(meetingsFolder);

            Folder projectsFolder = new Folder();
            projectsFolder.setName("Projects");
            projectsFolder.setDescription("Project documentation and tasks");
            projectsFolder.setCategory(workCategory);
            projectsFolder = folderRepository.save(projectsFolder);

            // Personal Category Folders
            Folder shoppingFolder = new Folder();
            shoppingFolder.setName("Shopping Lists");
            shoppingFolder.setDescription("Grocery and shopping reminders");
            shoppingFolder.setCategory(personalCategory);
            shoppingFolder = folderRepository.save(shoppingFolder);

            Folder journalFolder = new Folder();
            journalFolder.setName("Journal");
            journalFolder.setDescription("Daily thoughts and reflections");
            journalFolder.setCategory(personalCategory);
            journalFolder = folderRepository.save(journalFolder);

            // Study Category Folders
            Folder programmingFolder = new Folder();
            programmingFolder.setName("Programming");
            programmingFolder.setDescription("Code snippets and tutorials");
            programmingFolder.setCategory(studyCategory);
            programmingFolder = folderRepository.save(programmingFolder);

            // ========== Create Folders for John Doe ==========
            Folder webDevFolder = new Folder();
            webDevFolder.setName("Web Development");
            webDevFolder.setDescription("Web dev project notes");
            webDevFolder.setCategory(projectsCategory);
            webDevFolder = folderRepository.save(webDevFolder);

            Folder brainstormFolder = new Folder();
            brainstormFolder.setName("Brainstorming");
            brainstormFolder.setDescription("Random ideas and concepts");
            brainstormFolder.setCategory(ideasCategory);
            brainstormFolder = folderRepository.save(brainstormFolder);

            // ========== Create Notes for Demo User ==========
            // Meetings Folder Notes
            Note meetingNote1 = new Note();
            meetingNote1.setTitle("Project Kickoff Meeting");
            meetingNote1.setContent(
                    "## Attendees\n- Sarah Johnson\n- Mike Chen\n- Alex Rodriguez\n\n## Agenda\n1. Project overview\n2. Timeline discussion\n3. Resource allocation\n\n## Key Decisions\n- Launch date: March 15th\n- Budget approved: $50,000\n- Weekly standups: Every Monday 10 AM\n\n## Action Items\n- [ ] Mike: Set up project repository\n- [ ] Sarah: Draft requirements document\n- [ ] Alex: Schedule follow-up meeting");
            meetingNote1.setFolder(meetingsFolder);
            noteRepository.save(meetingNote1);

            Note meetingNote2 = new Note();
            meetingNote2.setTitle("Q1 Review Meeting");
            meetingNote2.setContent(
                    "## Performance Highlights\n- Revenue up 25% vs Q4\n- Customer satisfaction: 4.5/5\n- New clients: 12\n\n## Areas for Improvement\n- Response time (currently 48hrs, target 24hrs)\n- Documentation needs update\n\n## Q2 Goals\n- Expand team by 3 members\n- Launch new product feature\n- Improve support response time");
            meetingNote2.setFolder(meetingsFolder);
            noteRepository.save(meetingNote2);

            // Projects Folder Notes
            Note projectNote1 = new Note();
            projectNote1.setTitle("Website Redesign");
            projectNote1.setContent(
                    "## Project Overview\nComplete redesign of company website with modern UI/UX\n\n## Tech Stack\n- Frontend: React + TypeScript\n- Styling: Tailwind CSS\n- Backend: Node.js + Express\n- Database: PostgreSQL\n\n## Phases\n1. Discovery & Research (2 weeks)\n2. Design Mockups (3 weeks)\n3. Development (6 weeks)\n4. Testing & QA (2 weeks)\n5. Launch (1 week)\n\n## Budget: $30,000");
            projectNote1.setFolder(projectsFolder);
            noteRepository.save(projectNote1);

            // Shopping Folder Notes
            Note shoppingNote = new Note();
            shoppingNote.setTitle("Weekly Grocery List");
            shoppingNote.setContent(
                    "## Produce\n- Bananas\n- Apples\n- Spinach\n- Tomatoes\n- Avocados\n\n## Dairy\n- Milk (2%)\n- Greek yogurt\n- Cheese\n- Butter\n\n## Pantry\n- Pasta\n- Rice\n- Olive oil\n- Canned tomatoes\n\n## Other\n- Coffee beans\n- Bread\n- Eggs");
            shoppingNote.setFolder(shoppingFolder);
            noteRepository.save(shoppingNote);

            // Journal Folder Notes
            Note journalNote = new Note();
            journalNote.setTitle("Reflections - October 2025");
            journalNote.setContent(
                    "Today was a productive day. Completed the project proposal and got positive feedback from the team. Feeling grateful for the supportive work environment.\n\n## Things I'm grateful for:\n- Supportive colleagues\n- Interesting projects\n- Work-life balance\n\n## Tomorrow's focus:\n- Finish presentation slides\n- Review code changes\n- 1:1 with manager");
            journalNote.setFolder(journalFolder);
            noteRepository.save(journalNote);

            // Programming Folder Notes
            Note programmingNote = new Note();
            programmingNote.setTitle("Spring Boot Best Practices");
            programmingNote.setContent(
                    "## Project Structure\n- Use layered architecture (Controller → Service → Repository)\n- Keep controllers thin\n- Business logic in service layer\n\n## Security\n- Always use password encoding\n- Implement JWT authentication\n- Validate all inputs\n- Use @PreAuthorize for method-level security\n\n## Performance\n- Use pagination for large datasets\n- Implement caching where appropriate\n- Optimize database queries\n- Use DTOs to avoid exposing entities\n\n## Testing\n- Write unit tests for services\n- Integration tests for repositories\n- MockMvc for controller testing");
            programmingNote.setFolder(programmingFolder);
            noteRepository.save(programmingNote);

            // ========== Create Notes for John Doe ==========
            Note johnNote1 = new Note();
            johnNote1.setTitle("E-commerce Platform Ideas");
            johnNote1.setContent(
                    "## Core Features\n- User authentication & profiles\n- Product catalog with search\n- Shopping cart\n- Payment integration (Stripe)\n- Order tracking\n\n## Tech Stack Options\n- Frontend: Next.js or React\n- Backend: Spring Boot or Node.js\n- Database: PostgreSQL\n- Cloud: AWS or Azure\n\n## Monetization\n- Transaction fees (2-3%)\n- Premium seller accounts\n- Featured listings");
            johnNote1.setFolder(webDevFolder);
            noteRepository.save(johnNote1);

            Note johnNote2 = new Note();
            johnNote2.setTitle("AI Productivity Assistant");
            johnNote2.setContent(
                    "## Concept\nAI-powered assistant that helps users manage tasks, schedule meetings, and prioritize work.\n\n## Key Features\n- Natural language task creation\n- Smart scheduling based on calendar\n- Priority recommendations\n- Email integration\n- Slack/Teams integration\n\n## Competitive Advantage\n- Focus on simplicity\n- Privacy-first approach\n- Affordable pricing\n\n## MVP Scope\n- Task management\n- Calendar integration\n- Basic AI suggestions");
            johnNote2.setFolder(brainstormFolder);
            noteRepository.save(johnNote2);

            System.out.println("✅ Database initialized successfully!");
            System.out.println("\n📝 Example accounts:");
            System.out.println("   - admin@noteflow.com / admin123");
            System.out.println("   - demo@noteflow.com / demo123");
            System.out.println("   - john.doe@example.com / password123");
            System.out.println("\n📊 Data created:");
            System.out.println("   - Users: 3");
            System.out.println("   - Categories: 5");
            System.out.println("   - Folders: 7");
            System.out.println("   - Notes: 9");
        };
    }
}